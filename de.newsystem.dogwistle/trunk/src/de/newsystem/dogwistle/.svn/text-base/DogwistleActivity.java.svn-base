package de.newsystem.dogwistle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DogwistleActivity extends Activity {

	private Socket idefix;
	public DataOutputStream outstream;
	public DataInputStream instream;
	private TextView stateText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		stateText = (TextView) findViewById(R.id.text_state);
		stateText.setText(getResources().getString(R.string.str_disconnect));

		new ConnectToIdefix().execute(new Integer[] {});
	}

	private void execure(String cmd) {
		if (outstream == null) {
			Toast.makeText(DogwistleActivity.this, "Idefix nicht verbunden",
					Toast.LENGTH_LONG).show();
			return;
		}
		try {
			byte[] bytes = cmd.getBytes();
			outstream.write(bytes);
			Toast.makeText(DogwistleActivity.this, "Befehl wurde gesendet",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(DogwistleActivity.this,
					"Ein Fehler: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void backup(View v) {
		execure("<backup>");
		final ProgressBar pg = (ProgressBar) findViewById(R.id.pb_backup);
		pg.setVisibility(View.VISIBLE);

		new AsyncTask<Integer, Integer, String>() {

			@Override
			protected String doInBackground(Integer... params) {
				byte[] buffer = new byte[15];
				try {
					instream.read(buffer);
					return "Erfolgreicher Backup";
				} catch (IOException e) {
					e.printStackTrace();
					return e.getMessage();
				}

			}

			protected void onPostExecute(String result) {
				pg.setVisibility(View.INVISIBLE);
				Toast.makeText(DogwistleActivity.this, result,
						Toast.LENGTH_LONG).show();

			};
		}.execute(new Integer[] {});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = new MenuInflater(getApplication());
		mi.inflate(R.menu.main_pref, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.opt_shutdown:
			execure("<shutdown>");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class ConnectToIdefix extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			try {
				idefix = new Socket("kathi-basti.dyndns.org", 5001);
				outstream = new DataOutputStream(idefix.getOutputStream());
				instream = new DataInputStream(idefix.getInputStream());
			} catch (Exception e) {
				return "Ein Fehler: " + e.getMessage();
			}
			return "Verbunden";
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(DogwistleActivity.this, result, Toast.LENGTH_LONG)
					.show();
			if (result.equals("Verbunden"))
				stateText.setText(getResources()
						.getString(R.string.str_connect));
			super.onPostExecute(result);
		}

	}
}