package kr.co.plasticcity.declarativeviews.sample;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import kr.co.plasticcity.declarativeviews.DeclarativeRecyclerView;
import kr.co.plasticcity.declarativeviews.DeclarativeViewPager;
import kr.co.plasticcity.declarativeviews.ListModel;
import kr.co.plasticcity.declarativeviews.sample.databinding.DvpPageBinding;

public class DVPActivity extends AppCompatActivity
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
				    new Thread(() ->
				    {
					    dvp.build(pager ->
					    {
						    pager.setItemCount(6)
						         .setCircular()
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
						         .buildOnUiThread(() ->
						         {
							         dvp.setOffscreenPageLimit(1);
							         tab.setupWithViewPager(dvp);
						         });
					    });
				    }).start();
			    })
			    .apply()
			
			    .addGroup("", R.layout.dvp_item)
			    .onCreate(view ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    final TabLayout tab = view.findViewById(R.id.tab);
				    dvp.build(pager ->
				    {
					    dvp.post(() ->
					    {
						    pager.setItemCount(5)
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
						    dvp.setSwipeDisabled();
						    tab.setupWithViewPager(dvp);
						    next(dvp);
					    });
				    });
			    })
			    .apply()
			
			    .addGroup("", R.layout.dvp_item)
			    .onCreate(view ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    final TabLayout tab = view.findViewById(R.id.tab);
				    dvp.build(pager ->
				    {
					    dvp.post(() ->
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
							         Log.d("DeclarativeViewPager", "3rd: " + position);
						         })
						         .build();
						    dvp.setSwipeDisabled();
						    tab.setupWithViewPager(dvp);
						    prev(dvp);
					    });
				    });
			    })
			    .apply()
			
			    .addGroup("", R.layout.dvp_item_min_height)
			    .onCreate(view ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    dvp.build(pager ->
				    {
					    pager.setInfiniteMode(1)
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
						         Log.d("DeclarativeViewPager", "4th: " + position);
					         })
					         .build();
				    });
				    dvp.setCurrentItem(-1);
			    })
			    .apply()
			
			    .addGroup(ListModel.of(Arrays.asList(5, 6, 7, 8, 9)), R.layout.dvp_item)
			    .onCreate(view ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    final TabLayout tab = view.findViewById(R.id.tab);
				    dvp.build(pager ->
				    {
					    pager.setItemCount(7)
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
						         Log.d("DeclarativeViewPager", String.format("%sth: %d", dvp.getTag(), position));
					         })
					         .build();
				    });
				    tab.setupWithViewPager(dvp);
			    })
			    .onBind((view, pos) ->
			    {
				    final DeclarativeViewPager dvp = view.findViewById(R.id.dvp);
				    dvp.setTag(pos);
				    dvp.setCurrentItem(pos % 7);
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
	
	private void prev(final DeclarativeViewPager dvp)
	{
		dvp.postDelayed(() ->
		{
			dvp.showPrev();
			prev(dvp);
		}, 1500);
	}
}