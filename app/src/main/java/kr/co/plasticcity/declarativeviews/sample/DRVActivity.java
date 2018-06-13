package kr.co.plasticcity.declarativeviews.sample;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kr.co.plasticcity.declarativeviews.IdComparable;
import kr.co.plasticcity.declarativeviews.ListModel;
import kr.co.plasticcity.declarativeviews.SingleModel;
import kr.co.plasticcity.declarativeviews.sample.databinding.DrvActivityBinding;
import kr.co.plasticcity.declarativeviews.sample.databinding.DrvItemBinding;
import kr.co.plasticcity.declarativeviews.sample.databinding.DrvItemFooterBinding;

public class DRVActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final DrvActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.drv_activity);
		binding.dvp.build(builder ->
		{
			builder.setItemCount(3)
			       .setPageView(R.layout.drv_page, DRVPage.class)
			       .onPageCreated((DRVPage, position) ->
			       {
				       switch (position)
				       {
				       case 0:
					       buildFirstPage(DRVPage);
					       break;
				       case 1:
					       buildSecondPage(DRVPage);
					       break;
				       case 2:
					       binding.dvp.postDelayed(() -> new Thread(() -> buildThirdPage(DRVPage)).start(), 500);
					       break;
				       }
			       })
			       .build();
		});
	}
	
	private void buildFirstPage(@NonNull final DRVPage page)
	{
		final ListModel<Model> listModel = ListModel.of(new Model("0"));
		final SingleModel<String> footerModel = SingleModel.of("");
		
		page.binding.drv.build(builder ->
		{
			builder.addGroup(listModel, R.layout.drv_item, DrvItemBinding.class)
			       .onCreate(v ->
			       {
				       v.chk.setVisibility(View.VISIBLE);
			       })
			       .onBind((v, m, position) ->
			       {
				       v.txv.setText(m.value);
				       v.btn.setVisibility(View.VISIBLE);
				       v.btn.setOnClickListener(v1 ->
				       {
					       listModel.add(0, new Model("10"));
				       });
				       v.getRoot().setPadding(0, Integer.parseInt(m.value), 0, 0);
			       })
			       .setPlaceholder(2, v ->
			       {
				       v.txv.setText("Placeholder");
				       v.btn.setVisibility(View.GONE);
				       v.getRoot().setPadding(0, 0, 0, 0);
			       })
			       .setDividerExcludeLast(3, R.color.medium)
			       .apply()
			
			       .addFooter(footerModel, R.layout.drv_item_footer, DrvItemFooterBinding.class)
			       .onCreate(v ->
			       {
				       v.btnPositive.setOnClickListener(v1 -> listModel.add(new Model("0")));
			       })
			       .apply()
			
			       .build();
		});
		
		page.binding.btn1.setText("ADD");
		page.binding.btn1.setOnClickListener(v ->
		{
			listModel.add(new Model("0"));
		});
		page.binding.btn2.setText("ADDx3");
		page.binding.btn2.setOnClickListener(v ->
		{
			listModel.addAll(Arrays.asList(new Model("0"), new Model("0"), new Model("0")));
		});
		page.binding.btn3.setText("REMOVE");
		page.binding.btn3.setOnClickListener(v ->
		{
			if (!listModel.isEmpty())
			{
				listModel.remove(listModel.size() - 1);
			}
		});
		page.binding.btn4.setText("RETAIN");
		page.binding.btn4.setOnClickListener(v ->
		{
			if (!listModel.isEmpty())
			{
				listModel.retainAll(Arrays.asList(new Model("10")));
			}
		});
		page.binding.btn5.setText("CLEAR");
		page.binding.btn5.setOnClickListener(v ->
		{
			if (!listModel.isEmpty())
			{
				listModel.clear();
			}
		});
	}
	
	private void buildSecondPage(@NonNull final DRVPage page)
	{
		final SingleModel<String> m0 = SingleModel.of("그냥");
		final ListModel<Object> m1 = ListModel.of("", "", "");
		final ListModel<Model> listModel = ListModel.of(createRandomList());
		final ListModel<Object> m2 = ListModel.of("", "");
		final SingleModel<String> m3 = SingleModel.of("난 마지막?");
		
		page.binding.drv.build(builder ->
		{
			builder.addGroup(m0, R.layout.drv_item, DrvItemBinding.class)
			       .onBind((v, m) ->
			       {
				       v.txv.setText(m);
			       })
			       .apply()
			
			       .addGroup(m1, R.layout.drv_item, DrvItemBinding.class)
			       .onBind((v, m) ->
			       {
				       v.txv.setText("자리만 차지하는 애들");
			       })
			       .apply()
			
			       .addGroup(listModel, R.layout.drv_item, DrvItemBinding.class)
			       .onBind((v, m) ->
			       {
				       v.chk.setVisibility(View.VISIBLE);
				       v.txv.setText(m.value);
			       })
			       .setPlaceholder(10, v ->
			       {
				       v.chk.setVisibility(View.GONE);
				       v.txv.setText("Placeholder");
			       })
			       .apply()
			
			       .addGroup(m2, R.layout.drv_item, DrvItemBinding.class)
			       .onBind((v, m) ->
			       {
				       v.txv.setText("자리만 차지하는 애들");
			       })
			       .apply()
			
			       .addGroup(m3, R.layout.drv_item, DrvItemBinding.class)
			       .onBind((v, m) ->
			       {
				       v.txv.setText(m);
			       })
			       .apply()
			
			       .build();
		});
		
		page.binding.btn1.setText("UPDATE");
		page.binding.btn1.setOnClickListener(v ->
		{
			listModel.update(createRandomList());
		});
	}
	
	private void buildThirdPage(@NonNull final DRVPage page)
	{
		final SingleModel<String> emptyModel = SingleModel.empty();
		final SingleModel<?> nullModel = SingleModel.of(new Object());
		final SingleModel<String> singleModel = SingleModel.of("숫자 = 0");
		final SingleModel<String> addFront = SingleModel.of("앞에 아이템 추가");
		final SingleModel<String> addRear = SingleModel.of("뒤에 아이템 추가");
		final SingleModel<String> getPosition = SingleModel.of("포지션 계산");
		final ListModel<Model> listFront = ListModel.empty();
		final ListModel<Model> listRear = ListModel.empty();
		
		final Set<Model> selected = new HashSet<>();
		final List<Model> selectedFront = new ArrayList<>();
		final List<Model> selectedReal = new ArrayList<>();
		
		page.binding.drv.build(builder ->
		{
			builder.addGroup(listFront, R.layout.drv_item, DrvItemBinding.class)
			       .onCreate(v ->
			       {
				       v.chk.setVisibility(View.VISIBLE);
			       })
			       .onBind((v, m) ->
			       {
				       v.txv.setText(m.value);
				       v.btn.setText("삭제");
				       v.btn.setOnClickListener(v1 -> listFront.remove(m));
				       v.chk.setOnCheckedChangeListener(null);
				       v.chk.setChecked(selected.contains(m));
				       v.chk.setOnCheckedChangeListener((buttonView, isChecked) ->
				       {
					       if (isChecked)
					       {
						       selected.add(m);
						       selectedFront.add(m);
					       }
					       else
					       {
						       selected.remove(m);
						       selectedFront.remove(m);
					       }
				       });
			       })
			       .apply()
			
			       .addGroup("정적 모델", R.layout.drv_item, DrvItemBinding.class)
			       .onFistBind((v, m) ->
			       {
				       v.txv.setText(m);
				       v.btn.setText("빈 모델 추가");
				       v.btn.setOnClickListener(v1 -> emptyModel.set("빈 모델"));
			       })
			       .apply()
			
			       .addGroup(emptyModel, R.layout.drv_item, DrvItemBinding.class)
			       .onFistBind((v, m) ->
			       {
				       v.txv.setText(m);
				       v.btn.setText("삭제");
				       v.btn.setOnClickListener(v1 -> emptyModel.remove());
			       })
			       .apply()
			
			       .addGroup(nullModel, R.layout.drv_item, DrvItemBinding.class)
			       .onCreate(v ->
			       {
				       v.txv.setText("널 모델");
				       v.btn.setText("삭제");
				       v.btn.setOnClickListener(v1 -> nullModel.remove());
			       })
			       .apply()
			
			       .addGroup(singleModel, R.layout.drv_item, DrvItemBinding.class)
			       .onCreate(v ->
			       {
				       v.btn.setText("숫자 바꾸기");
				       v.btn.setOnClickListener(v1 ->
				       {
					       singleModel.replace(m -> String.format("숫자 = %s", ((int)(Math.random() * 1000))));
				       });
			       })
			       .onBind((v, m) ->
			       {
				       v.txv.setText(m);
			       })
			       .apply()
			
			       .addGroup(addFront, R.layout.drv_item, DrvItemBinding.class)
			       .onFistBind((v, m) ->
			       {
				       v.txv.setText(m);
				       v.btn.setText(m);
				       v.btn.setOnClickListener(v1 -> listFront.add(0, new Model("" + new Object().hashCode())));
			       })
			       .apply()
			
			       .addGroup(addRear, R.layout.drv_item, DrvItemBinding.class)
			       .onFistBind((v, m) ->
			       {
				       v.txv.setText(m);
				       v.btn.setText(m);
				       v.btn.setOnClickListener(v1 -> listRear.add(new Model("" + new Object().hashCode())));
			       })
			       .apply()
			
			       .addGroup(getPosition, R.layout.drv_item, DrvItemBinding.class)
			       .onCreate(v ->
			       {
				       v.btn.setOnClickListener(v1 -> getPosition.performChanged());
			       })
			       .onBind((v, m) ->
			       {
				       v.txv.setText(String.format("나의 포지션 = %s", getPosition.getPositionInList()));
				       v.btn.setText("포지션 계산");
			       })
			       .apply()
			
			       .addGroup(SingleModel.of(""), R.layout.drv_item, DrvItemBinding.class)
			       .onCreate(v ->
			       {
				       v.txv.setText("워프하기");
				       v.btn.setText("이동");
				       v.btn.setOnClickListener(v1 ->
				       {
					       final int positionToMove = (int)(Math.random() * page.binding.drv.getItemCount());
					       page.binding.drv.smoothScrollToPosition(positionToMove);
					       v.txv.setText(String.format("%s/%s(으)로 이동했었다", positionToMove, page.binding.drv.getItemCount()));
				       });
			       })
			       .apply()
			
			       .addGroup(listRear, R.layout.drv_item, DrvItemBinding.class)
			       .onCreate(v ->
			       {
				       v.chk.setVisibility(View.VISIBLE);
			       })
			       .onBind((v, m) ->
			       {
				       v.txv.setText(m.value);
				       v.btn.setText("삭제");
				       v.btn.setOnClickListener(v1 -> listRear.remove(m));
				       v.chk.setOnCheckedChangeListener(null);
				       v.chk.setChecked(selected.contains(m));
				       v.chk.setOnCheckedChangeListener((buttonView, isChecked) ->
				       {
					       if (isChecked)
					       {
						       selected.add(m);
						       selectedReal.add(m);
					       }
					       else
					       {
						       selected.remove(m);
						       selectedReal.remove(m);
					       }
				       });
			       })
			       .setDividerExcludeLast(3, R.color.medium)
			       .apply()
			
			       .buildOnUiThread(() ->
			       {
				       page.binding.btn1.setText("REMOVE");
				       page.binding.btn1.setOnClickListener(v ->
				       {
					       listFront.removeAll(selected);
					       listRear.removeAll(selected);
					       selected.clear();
					       selectedFront.clear();
					       selectedReal.clear();
				       });
				
				       page.binding.btn2.setText("RETAIN");
				       page.binding.btn2.setOnClickListener(v ->
				       {
					       listFront.retainAll(selected);
					       listRear.retainAll(selected);
				       });
				
				       page.binding.btn3.setText("MOVE");
				       page.binding.btn3.setOnClickListener(v ->
				       {
					       if (selectedFront.size() == 2)
					       {
						       listFront.move(listFront.indexOf(selectedFront.get(0)), listFront.indexOf(selectedFront.get(1)));
					       }
					       if (selectedReal.size() == 2)
					       {
						       listRear.move(listRear.indexOf(selectedReal.get(0)), listRear.indexOf(selectedReal.get(1)));
					       }
				       });
				
				       page.binding.btn4.setText("RESET");
				       page.binding.btn4.setOnClickListener(v ->
				       {
					       selected.clear();
					       selectedFront.clear();
					       selectedReal.clear();
					       listFront.replaceRange(0, listFront.size() - 1, (m, position) -> new Model(position + ": front"));
					       listRear.replaceRange(0, listRear.size() - 1, (m, position) -> new Model(position + ": real"));
				       });
			       });
		});
	}
	
	private List<Model> createRandomList()
	{
		final List<Model> list = new ArrayList<>();
		for (int i = 0 ; i < 20 ; ++i)
		{
			if ((int)(Math.random() * 5) < 2)
			{
				list.add(new Model("" + i));
			}
		}
		return list;
	}
	
	private class Model implements IdComparable<Model>
	{
		@NonNull
		private final String value;
		
		private Model(@NonNull final String value)
		{
			this.value = value;
		}
		
		@Override
		public boolean hasSameId(@NonNull final Model model)
		{
			return value.equals(model.value);
		}
		
		@Override
		public boolean equals(final Object o)
		{
			if (this == o) { return true; }
			if (o == null || getClass() != o.getClass()) { return false; }
			
			Model model = (Model)o;
			
			return value.equals(model.value);
		}
		
		@Override
		public int hashCode()
		{
			return value.hashCode();
		}
	}
	
	private static class Divider extends RecyclerView.ItemDecoration
	{
		@NonNull
		private final GradientDrawable lineDrawable;
		private final int lineHeight;
		
		public Divider(@NonNull final Context context, final float heightDp)
		{
			this.lineDrawable = new GradientDrawable();
			this.lineDrawable.setColor(0xffff0000);
			this.lineHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp, context.getResources().getDisplayMetrics()));
		}
		
		@Override
		public void onDraw(final Canvas c, final RecyclerView parent, final RecyclerView.State state)
		{
			super.onDraw(c, parent, state);
			
			final int lineLeft = parent.getPaddingLeft();
			final int lineRight = parent.getWidth() - parent.getPaddingRight();
			final int childCount = parent.getChildCount();
			for (int i = 0 ; i < childCount ; ++i)
			{
				final View child = parent.getChildAt(i);
				final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
				final int lineTop = child.getBottom() + params.bottomMargin + Math.round(child.getTranslationY());
				final int lineBottom = lineTop + lineHeight;
				lineDrawable.setBounds(lineLeft, lineTop, lineRight, lineBottom);
				lineDrawable.draw(c);
			}
		}
		
		@Override
		public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state)
		{
			if (parent.getChildAdapterPosition(view) < parent.getAdapter().getItemCount() - 1)
			{
				outRect.bottom = lineHeight;
			}
		}
	}
}