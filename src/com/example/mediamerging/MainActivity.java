package com.example.mediamerging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Button recordButton, playButton;
	boolean recording = false, playing = false;
	private static String mFileName = null;
	String sdCard;
	
	EditText fileName;

	private MediaRecorder mRecorder = null;

	private MediaPlayer mPlayer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sdCard = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		
		mergeTwoFiles(sdCard+"/test4.mp3", sdCard+"/test5.mp3", sdCard+"/test6.mp3");
		mRecorder = new MediaRecorder();
		
		fileName = (EditText)findViewById(R.id.editText1);
		
		recordButton = (Button) findViewById(R.id.button1);
		recordButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!recording) {
					startRecording();
					recording = true;
					recordButton.setText("Recording");
				} else {
					stopRecording();
					recording = false;
					recordButton.setText("Record");
				}
			}
		});

		playButton = (Button) findViewById(R.id.button2);
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!playing) {
					startPlaying();
					playing = true;
					playButton.setText("Playing");
				} else {
					stopPlaying();
					playing = false;
					playButton.setText("Play");
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e("Issue", "prepare() failed");
		}
	}

	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording() {
		String status = Environment.getExternalStorageState();
		if (status.equals("mounted")) {
			mFileName = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/"+fileName.getText().toString();

			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(mFileName);
			try {
				mRecorder.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mRecorder.start();
		}
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		//ArrayList<String> uris = new ArrayList<String>();
	//	mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		//mFileName += "/testAkhil.3gp";
		//uris.add(mFileName);

	}
	
	
	private void mergeTwoFiles(String file1, String file2, String outFile){
		
		
		  try {
              FileInputStream fis1 = new FileInputStream(file1);
              FileInputStream fis2 = new FileInputStream(file2);
              SequenceInputStream sis = new SequenceInputStream(fis1,fis2);


              FileOutputStream fos = new FileOutputStream(new File(outFile));

              int temp;

              try {
                  while ((temp = sis.read())!= -1){

                      fos.write(temp);

                  }
              } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }




          } catch (FileNotFoundException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
		
	}
	
	
}
