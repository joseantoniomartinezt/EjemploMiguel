package com.idme.myprbtabsfragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.idme.myprbtabsfragments.ui.main.PlaceholderFragment;
import com.idme.myprbtabsfragments.ui.main.SectionsPagerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
	TabLayout tabs;
	SectionsPagerAdapter sectionsPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

		ViewPager viewPager = findViewById(R.id.view_pager);
		viewPager.setAdapter(sectionsPagerAdapter);

		tabs = findViewById(R.id.tabs);
		tabs.setupWithViewPager(viewPager);

		permisos();
	}

	public void permisos()
	{
		try
		{
			String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
			requestPermissions(permissions, 1);
		}
		catch (Exception e)
		{
			mostrarAlerta( "Error", e.toString());
		}
	}

	String filenameFoto = android.os.Environment.getExternalStorageDirectory() + File.separator+ "PRUEBA_TABS" + File.separator+ "temp.jpg";

	public void tomarFoto(View view)
	{
		try
		{
			permisos();


			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			File f = new File(filenameFoto);
			Uri fp = FileProvider.getUriForFile(getApplicationContext(), this.getApplicationContext().getPackageName() + ".provider", f);

			new File(f.getParent()).mkdirs();

			//File.createTempFile("PRB", "NADA", android.os.Environment.getExternalStorageDirectory());

			intent.putExtra(MediaStore.EXTRA_OUTPUT, fp);
			//intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			startActivityForResult(intent, 1);
		}
		catch (Exception e)
		{
			String ex = e.getMessage();
			mostrarAlerta("tomarFoto", ex);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		try
		{
			super.onActivityResult(requestCode, resultCode, data);


			if (resultCode == RESULT_OK)
			{
				///camara
				if (requestCode == 1)
				{
					File f = new File(filenameFoto);

					try
					{
						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
						Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

						String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "PRUEBA_TABS" + File.separator;
						OutputStream outFile = null;
						File file = new File(path);
						file.mkdirs();
						file = new File(path, "PRUEBA_" + new SimpleDateFormat("dd-MM-yyyy_HHmmss").format(Calendar.getInstance().getTime()) + ".jpg");

						outFile = new FileOutputStream(file);
						System.gc();

						bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outFile);
						System.gc();
						outFile.flush();
						outFile.close();

						f.delete();

						///hago que se actualize la galeria
						Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
						Uri contentUri = Uri.fromFile(file);
						mediaScanIntent.setData(contentUri);
						this.sendBroadcast(mediaScanIntent);

						ImageView imgv = findViewById(R.id.imageViewFoto);
						imgv.setImageBitmap(bitmap);
					}
					catch (Exception e)
					{
						mostrarAlerta("activityResult", e.getMessage());
					}
				}



			}
		}
		catch (Exception e)
		{

		}
	}


	public void siguientePagina(View view)
	{
		int pos = tabs.getSelectedTabPosition() + 1;

		if (pos < sectionsPagerAdapter.getCount())
			tabs.getTabAt(pos).select();
	}

	public void anteriorPagina(View view)
	{
		int pos = tabs.getSelectedTabPosition() - 1;

		if (pos >= 0)
			tabs.getTabAt(pos).select();
	}

	public void mostrarAlerta(String asunto, String texto)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(asunto);
		builder.setMessage(texto);
		builder.setPositiveButton("OK", null);
		builder.create();
		builder.show();
	}
}