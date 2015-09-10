package com.nemory.bundledfun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nemory.bundledfun.R;
import com.nemory.bundledfun.helpers.Constants;
import com.nemory.bundledfun.objects.Question;

public class QuizActivity extends Activity implements OnClickListener {
	
	private Button btnA, btnB, btnC, btnSkip;
	private TextView tvTimer, tvScore, tvCorrectAnswers, tvQuestion;
	private CountDownTimer timer;
	private Chronometer chTimeElapsed;
	private LinearLayout lQuestionContainer;
	private LinearLayout.LayoutParams lParams;
	private MapView mvMap;
	private MapController mapController;
	private MediaPlayer mp;
	private Question currentQuestion;
	private int currentScore = 0;
	private HashMap<String, Integer> sounds;
	private SoundPool mSoundPool;
	private int totalQuestions;
  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.quiz);
		this.initialize();
		this.prompt();
	}
	
	@SuppressWarnings("static-access")
	private void prompt(){
		AlertDialog d = new AlertDialog.Builder(this).create();
		d.setCancelable(false);
		d.setTitle("Start the game?");
		d.setMessage("Be ready for the \"Bundled Fun Questions.\"");

		d.setButton(d.BUTTON_POSITIVE, "Start", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startGame();
			}
		});
		
		d.show();
	}
	
	@SuppressLint("SdCardPath")
	private void replaceView(){
		View view = lQuestionContainer.getChildAt(1);
		
		tvQuestion.setText(currentQuestion.getText());
		
		if(currentQuestion.getType().equals("text")){
			tvQuestion.setVisibility(View.GONE);
			mvMap.setVisibility(View.GONE);
			lQuestionContainer.removeView(view);
			
			TextView textView = new TextView(this);
			textView.setLayoutParams(lParams);
			textView.setTextSize(30);
			textView.setGravity(Gravity.CENTER);
			textView.setText(currentQuestion.getText());
			lQuestionContainer.addView(textView, 1);
			
		}else if(currentQuestion.getType().equals("image")){
			tvQuestion.setVisibility(View.VISIBLE);
			mvMap.setVisibility(View.GONE);
			lQuestionContainer.removeView(view);
			
		    ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(lParams);
			Uri imageUri = Uri.fromFile(new File(Constants.DATAPATH  + currentQuestion.getDifficulty() + "/images/" + currentQuestion.getFileName()));
			imageView.setImageURI(imageUri);
			lQuestionContainer.addView(imageView, 1);
			
		}else if(currentQuestion.getType().equals("video")){
			tvQuestion.setVisibility(View.VISIBLE);
			tvQuestion.setText(currentQuestion.getText());
			mvMap.setVisibility(View.GONE);
			lQuestionContainer.removeView(view);
			
			VideoView videoView = new VideoView(this);
			videoView.setVideoPath(Constants.DATAPATH + currentQuestion.getDifficulty() + "/videos/" + currentQuestion.getFileName());
	        videoView.setLayoutParams(lParams);
	        videoView.setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					mp.setLooping(true);
				}
			});

	        lQuestionContainer.addView(videoView, 1);
		    videoView.start();
		    
		}else if(currentQuestion.getType().equals("audio")){
			tvQuestion.setVisibility(View.GONE);
			mvMap.setVisibility(View.GONE);
			lQuestionContainer.removeView(view);
			
			TextView textView = new TextView(this);
			textView.setLayoutParams(lParams);
			textView.setTextSize(30);
			textView.setGravity(Gravity.CENTER);
			textView.setText(currentQuestion.getText());
			lQuestionContainer.addView(textView, 1);
			
			try {
				mp = new MediaPlayer();
				mp.setDataSource(Constants.DATAPATH + currentQuestion.getDifficulty() + "/audios/" + currentQuestion.getFileName());
				mp.setLooping(true);
				mp.prepare();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mp.start();
			
		}else if(currentQuestion.getType().equals("location")){
			tvQuestion.setVisibility(View.VISIBLE);
			view.setVisibility(View.GONE);
			mvMap.setVisibility(View.VISIBLE);
			String location[] = currentQuestion.getAnswer().split(",");
			mapController.animateTo(new GeoPoint(Double.parseDouble(location[0]), Double.parseDouble(location[1])));
		}
	}
	
	@SuppressWarnings("static-access")
	private void nextQuestion(String skip){
		if(mp != null){
			mp.release();
			mp = null;
		}
		
		if(Question.questions.size() > 0){
			Question.shuffle();
			currentQuestion = Question.questions.get(0);
			this.replaceView();
			btnA.setText(currentQuestion.getChoice_a());
			btnB.setText(currentQuestion.getChoice_b());
			btnC.setText(currentQuestion.getChoice_c());
			if(timer != null){ timer.cancel(); }
			this.setCountDownTimer(currentQuestion.getTimer());
			timer.start();
			if(skip.equals("no skip")){Question.questions.remove(0);}
		}else {
			chTimeElapsed.stop();
			String scoreCritics = "";
			
			if(currentScore == 100){
				scoreCritics = "Perfect";
			}else if(currentScore >= 80){
				scoreCritics = "Great";
			}else if(currentScore >= 40 && currentScore < 80){
				scoreCritics = "Good";
			}else if(currentScore < 40){
				scoreCritics = "Better luck next time dude. :)";
			}
			
			AlertDialog d = new AlertDialog.Builder(this).create();
			d.setCancelable(false);
			d.setTitle(scoreCritics);
			d.setMessage("Thanks for having fun! \n You got a score of " + currentScore + " \n What would you like to do next?");
			d.setButton(d.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// retry
				}
			});
			
			d.setButton(d.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// exit
				}
			});
			
			d.show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.nav_bar, menu);
		return true;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_quit){
			AlertDialog d = new AlertDialog.Builder(this).create();
			d.setTitle("Warning");
			d.setMessage("Nothing will be saved in this current quiz if you quit without finishing.");

			d.setButton(d.BUTTON_POSITIVE, "Quit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			
			d.setButton(d.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			
			d.show();
		}else if(item.getItemId() == R.id.menu_students){
			startActivity(new Intent(QuizActivity.this, UsersActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void compareAnswer(String answer){
		if(answer.equals(currentQuestion.getAnswer())){
			this.updateScore();
			this.playSound("correct");
		}else{
			this.playSound("wrong");
		}
		this.nextQuestion("no skip");
	}
	
	private void playSound(String sound){
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(sounds.get(sound), volume, volume, 1, 0, 1f);
	}
	
	private void updateScore(){
		currentScore += currentQuestion.getPoints();
		tvScore.setText("" + currentScore);
		tvCorrectAnswers.setText(Question.questions.size() + "/" + totalQuestions);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnA:
			compareAnswer(currentQuestion.getChoice_a());
			break;
			
		case R.id.btnB:
			compareAnswer(currentQuestion.getChoice_b());
			break;
			
		case R.id.btnC:
			compareAnswer(currentQuestion.getChoice_c());
			break;
			
		case R.id.btnSkip:
			if(Question.questions.size() > 1){
				nextQuestion("yes skip");
			}else{
				Toast.makeText(this, "cannot skip anymore", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	private void startGame(){
		this.nextQuestion("no skip");
		chTimeElapsed.start();
	}
	
	private void setCountDownTimer(long ms){
		timer = new CountDownTimer(ms, 1000) {
		     public void onTick(long ms) {
		         tvTimer.setText(ms / 1000 + "");
		     }

		     public void onFinish() {
		    	 nextQuestion("no skip");
		     }
		  };
	}
	
	private void initialize(){
		
		// TEXTVIEWS
		tvTimer 			= (TextView) findViewById(R.id.tvTimer);
		tvScore 			= (TextView) findViewById(R.id.tvScore);
		tvCorrectAnswers 	= (TextView) findViewById(R.id.tvCorrectAnswers);
		tvQuestion 			= (TextView) findViewById(R.id.tvQuestion);
		
		// BUTTON VIEWS
		btnA = (Button) findViewById(R.id.btnA);
		btnB = (Button) findViewById(R.id.btnB);
		btnC = (Button) findViewById(R.id.btnC);
		btnSkip = (Button) findViewById(R.id.btnSkip);
		
		// MAP
		mvMap = (MapView) findViewById(R.id.mvMap);
		mvMap.setTileSource(TileSourceFactory.MAPNIK);
		mvMap.setMultiTouchControls(true);
		mvMap.setBuiltInZoomControls(true);
		
		mapController = mvMap.getController();
		mapController.setZoom(8);
		
		//CHRONOMETER ELAPSED TIME TIMER
		chTimeElapsed = (Chronometer) findViewById(R.id.chTimeElapsed);
		
		btnA.setOnClickListener(this);
		btnB.setOnClickListener(this);
		btnC.setOnClickListener(this);
		btnSkip.setOnClickListener(this);
		
		lQuestionContainer = (LinearLayout) findViewById(R.id.lQuestionContainer);
		lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lParams.gravity = Gravity.CENTER;
		timer = null;
		
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		sounds = new HashMap<String, Integer>();
		sounds.put("wrong", mSoundPool.load(this, R.raw.wrong, 1));
		sounds.put("correct", mSoundPool.load(this, R.raw.correct, 1));
		
		totalQuestions = Question.questions.size();
		tvCorrectAnswers.setText("0/" + totalQuestions);
	}
	
	@SuppressWarnings("unused")
	private void download(){
		try {
			URL url = new URL("http://csntrvoting.x10.mx/xx.zip");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			File SDCardRoot = Environment.getExternalStorageDirectory();
			File file = new File(SDCardRoot,"xx.zip");
			FileOutputStream fileOutput = new FileOutputStream(file);
			InputStream inputStream = urlConnection.getInputStream();
			int totalSize = urlConnection.getContentLength();
			int downloadedSize = 0;
			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				//updateProgress(downloadedSize, totalSize);
				Log.d("DOWNLOAD", "downloaded: " + downloadedSize + ", total: " + totalSize);
			}
			
			fileOutput.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}