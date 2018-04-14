package kr.co.plasticcity.declarativeviews.sample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kr.co.plasticcity.declarativeviews.sample.databinding.ActivityMainBinding;
import kr.co.plasticcity.declarativeviews.sample.recyclerview.DRVSampleActivity;
import kr.co.plasticcity.declarativeviews.sample.viewpager.DVPSampleActivity;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		
		binding.button.setOnClickListener(v -> startActivity(new Intent(this, DRVSampleActivity.class)));
		binding.button2.setOnClickListener(v -> startActivity(new Intent(this, DVPSampleActivity.class)));
		binding.button3.setOnClickListener(v -> startActivity(new Intent(this, DVPSampleActivity.class)));
	}
}
