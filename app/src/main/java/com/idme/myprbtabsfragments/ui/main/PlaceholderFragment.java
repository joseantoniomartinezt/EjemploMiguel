package com.idme.myprbtabsfragments.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.idme.myprbtabsfragments.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment
{

	private static final String ARG_SECTION_NUMBER = "section_number";

	private int PAGINA;

	public static PlaceholderFragment newInstance(int index)
	{
		PlaceholderFragment fragment = new PlaceholderFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ARG_SECTION_NUMBER, index);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		int index = 0;
		if (getArguments() != null)
		{
			index = getArguments().getInt(ARG_SECTION_NUMBER);
		}

		PAGINA = index;
	}

	@Override
	public View onCreateView(
			@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		View root;
		if (SectionsPagerAdapter.esPagina(PAGINA, "texto"))
		{
			root = inflater.inflate(R.layout.texto1, container, false);
			TextView textView = root.findViewById(R.id.textViewTitulo);
			textView.setText("Texto libre");
		}
		else if (SectionsPagerAdapter.esPagina(PAGINA, "imagenes"))
		{
			root = inflater.inflate(R.layout.imagen1, container, false);
			TextView textView = root.findViewById(R.id.textViewTitulo);
			textView.setText("Añade una imagen");
		}
		else
		{
			root = inflater.inflate(R.layout.fragment_main, container, false);
			TextView textView = root.findViewById(R.id.section_label);
			textView.setText("Pestaña " + (PAGINA+1));
		}

		return root;
	}
}