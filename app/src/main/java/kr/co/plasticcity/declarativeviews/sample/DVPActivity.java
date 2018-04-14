package kr.co.plasticcity.declarativeviews.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import kr.co.plasticcity.declarativeviews.DeclarativeRecyclerView;
import kr.co.plasticcity.declarativeviews.ListModel;
import kr.co.plasticcity.declarativeviews.sample.databinding.DvpPageBinding;
import kr.co.plasticcity.declarativeviews.DeclarativeViewPager;

public class DVPActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dvp_activity);
		
		final DeclarativeRecyclerView rcv = findViewById(R.id.rcv);
		rcv.setHasFixedSize(true);
		rcv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
		rcv.build(list ->
		{
			list.addGroup("", R.layout.dvp_item)
			    .onCreate(view ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    final TabLayout tab = view.findViewById(R.id.tab);
				    dvp.build(pager ->
				    {
					    pager.setItemCount(6)
					         .setPageView(R.layout.dvp_page, DvpPageBinding.class)
					         .onPageCreated((v, position) ->
					         {
						         int color = 0xFF777777 | (0x000000BB << (position % 3 * 8));
						         v.pnl.setBackgroundColor(color);
						         v.txv.setOnClickListener(view1 -> dvp.reset());
						         v.txv.setText("Page " + position);
					         })
					         .onPageSelected(position ->
					         {
						         Log.d("DeclarativeViewPager", "1st: " + position);
					         })
					         .build();
				    });
				    tab.setupWithViewPager(dvp);
			    })
			    .apply()
			
			    .addGroup("", R.layout.dvp_item)
			    .onCreate(view ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    final TabLayout tab = view.findViewById(R.id.tab);
				    dvp.build(pager ->
				    {
					    pager.setItemCount(6)
					         .setCircular()
					         .setPageView(R.layout.dvp_page)
					         .onPageCreated((v, position) ->
					         {
						         final View pnl = v.findViewById(R.id.pnl);
						         final TextView txv = v.findViewById(R.id.txv);
						         int color = 0xFF777777 | (0x000000BB << (position % 3 * 8));
						         pnl.setBackgroundColor(color);
						         txv.setOnClickListener(view1 -> dvp.reset());
						         txv.setText("Page " + position);
					         })
					         .onPageSelected(position ->
					         {
						         Log.d("DeclarativeViewPager", "2nd: " + position);
					         })
					         .build();
				    });
				    tab.setupWithViewPager(dvp);
				    next(dvp);
			    })
			    .apply()
			
			    .addGroup("", R.layout.dvp_item_min_height)
			    .onCreate(view ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    dvp.build(pager ->
				    {
					    pager.setInfiniteMode(3)
					         .setPageView(R.layout.dvp_page, View.class)
					         .onPageCreated((v, position) ->
					         {
						         final View pnl = v.findViewById(R.id.pnl);
						         final TextView txv = v.findViewById(R.id.txv);
						         int color = 0xFF777777 | (0x000000BB << (position % 3 * 8));
						         pnl.setBackgroundColor(color);
						         txv.setOnClickListener(view1 -> dvp.reset());
						         txv.setText("Page " + position);
					         })
					         .onPageSelected(position ->
					         {
						         Log.d("DeclarativeViewPager", "3rd: " + position);
					         })
					         .setVertical()
					         .build();
				    });
			    })
			    .apply()
			
			    .addGroup("", R.layout.dvp_item_min_height)
			    .onCreate(view ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    dvp.build(pager ->
				    {
					    pager.setInfiniteMode(3)
					         .setPageView(() -> new TextView(this))
					         .onPageCreated((v, position) ->
					         {
						         v.setText("Page " + position);
					         })
					         .onPageSelected(position ->
					         {
						         Log.d("DeclarativeViewPager", "3rd: " + position);
					         })
					         .build();
				    });
			    })
			    .apply()
			
			    .addGroup(ListModel.of(Arrays.asList("", "", "")), R.layout.dvp_item)
			    .onCreate(view ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    final TabLayout tab = view.findViewById(R.id.tab);
				    dvp.build(pager ->
				    {
					    pager.setItemCount(6)
					         .setPageView(R.layout.dvp_page)
					         .onPageCreated((v, position) ->
					         {
						         final View pnl = v.findViewById(R.id.pnl);
						         final TextView txv = v.findViewById(R.id.txv);
						         int color = 0xFF777777 | (0x000000BB << (position % 3 * 8));
						         pnl.setBackgroundColor(color);
						         txv.setOnClickListener(view1 -> dvp.reset());
						         txv.setText("Page " + position);
					         })
					         .onPageSelected(position ->
					         {
						         Log.d("DeclarativeViewPager", "1st: " + position);
					         })
					         .build();
				    });
				    tab.setupWithViewPager(dvp);
			    })
			    .apply()
			
			    .build();
		});
	}
	
	private void next(final DeclarativeViewPager dvp)
	{
		dvp.postDelayed(() ->
		{
			dvp.showNext();
			next(dvp);
		}, 1500);
	}
}