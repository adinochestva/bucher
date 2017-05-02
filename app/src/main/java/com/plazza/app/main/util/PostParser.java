package com.plazza.app.main.util;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plazza.app.main.ImageActivity;
import com.plazza.app.main.MyApp;
import com.plazza.app.main.R;

public class PostParser {
	private MyApp appState;
	private Activity activity;

	public PostParser(Activity a, MyApp appState) {
		this.appState = appState;
		activity = a;
	}

	public void showBBCode(LinearLayout parent, String msg) {
		// persianize
		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append("\u200F");
		Boolean next_do = false;
		for (int loop = 0; loop < (msg.length()); loop++) {

			if (next_do) {
				next_do = false;
				sb.append("\u200F");
			}
			sb.append(msg.charAt(loop));

			if (msg.subSequence(loop, loop + 1).toString()
					.equalsIgnoreCase("\n"))
				next_do = true;
		}

		Spanned spanned = Html.fromHtml(bbcode(sb.toString()));
		SpannableStringBuilder htmlSpannable;
		// we need a SpannableStringBuilder for later use
		if (spanned instanceof SpannableStringBuilder) {
			// for now Html.fromHtml() returns a SpannableStringBuiler
			// so we can just cast it
			htmlSpannable = (SpannableStringBuilder) spanned;
		} else {
			// but we have a fallback just in case this will change later
			// or a custom subclass of Html is used
			htmlSpannable = new SpannableStringBuilder(spanned);
		}

		Boolean gotImgSpan = false;
		parent.removeAllViews();
		int start = 0;
		int end = 0;
		int imgz = 0;

		for (final ImageSpan img : htmlSpannable.getSpans(0,
				htmlSpannable.length(), ImageSpan.class)) {
			gotImgSpan = true;
			int Spanstart = htmlSpannable.getSpanStart(img);
			end = htmlSpannable.getSpanEnd(img);

			if (start != Spanstart) {
				TextView valueTV = new TextView(activity);
				valueTV.setMovementMethod(LinkMovementMethod.getInstance());
				valueTV.setClickable(true);
				valueTV.setId(5);
				valueTV.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

				valueTV.setTextColor(activity.getResources().getColor(
						R.color.Detail));
				valueTV.setGravity(Gravity.RIGHT);
				valueTV.setTextSize(appState.getFontSize());
				valueTV.setTypeface(appState.kodakFont);
				// valueTV.setAutoLinkMask(Linkify.ALL);
				// valueTV.setText(holder.htmlSpannable.toString().substring(
				// start, Spanstart));
				valueTV.setText((SpannableStringBuilder) htmlSpannable
						.subSequence(start, Spanstart),
						TextView.BufferType.SPANNABLE);
				parent.addView(valueTV);

			}

			ImageView valueTV = new ImageView(activity);
			valueTV.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			valueTV.setImageResource(android.R.color.transparent);
			valueTV.setTag(img.getSource());
			valueTV.setImageResource(activity.getResources().getIdentifier(
					img.getSource(), "drawable", activity.getPackageName()));
			valueTV.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Intent i = new Intent(activity, ImageActivity.class);
					i.putExtra("img", img.getSource());
					activity.startActivity(i);
				}
			});

			parent.addView(valueTV);

			// ImageView valueTV = new ImageView(activity);
			// valueTV.setLayoutParams(new LayoutParams(200, 300));
			// holder.body.addView(valueTV);
			//
			// valueTV.setImageResource(android.R.color.transparent);
			// valueTV.setTag(img.getSource());
			// imgloader.DisplayImage(img.getSource(), activity, valueTV);

			htmlSpannable.removeSpan(img);
			htmlSpannable.setSpan(null, Spanstart, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			htmlSpannable.replace(Spanstart, end, "");
			start = Spanstart;
		}

		if (!gotImgSpan) {
			TextView valueTV = new TextView(activity);
			valueTV.setMovementMethod(LinkMovementMethod.getInstance());
			valueTV.setClickable(true);
			valueTV.setId(5);
			valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			valueTV.setTextColor(activity.getResources().getColor(
					R.color.Detail));
			valueTV.setGravity(Gravity.RIGHT);
			// valueTV.setAutoLinkMask(Linkify.ALL);
			// valueTV.setGravity(Gravity.RIGHT);
			valueTV.setTextSize(appState.getFontSize());
			valueTV.setTypeface(appState.kodakFont);
			// String styledText =
			// "<font color='#ff6600' >.‫ را انتخاب نماید‬Troubleshoot ‫ گزینه‬Choose an option ‫5- در صفحه آبی رنگ‬</font>";
			// String styledText = "در صفحه chose گزینه ssfv را انتخاب کنید";
			// valueTV.setText("\u200F" + styledText);
			valueTV.setText(htmlSpannable, TextView.BufferType.SPANNABLE);

			parent.addView(valueTV);
		} else {
			String val = htmlSpannable.toString();
			if (val.length() != end) {
				TextView valueTV = new TextView(activity);
				valueTV.setMovementMethod(LinkMovementMethod.getInstance());
				valueTV.setClickable(true);
				valueTV.setId(5);
				valueTV.setLayoutParams(new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

				valueTV.setTextColor(activity.getResources().getColor(
						R.color.Detail));
				valueTV.setGravity(Gravity.RIGHT);
				valueTV.setTextSize(appState.getFontSize());
				valueTV.setTypeface(appState.kodakFont);
				// valueTV.setAutoLinkMask(Linkify.ALL);
				// valueTV.setText(holder.htmlSpannable.toString().substring(
				// end));
				valueTV.setText(htmlSpannable.replace(0, end - 1, ""),
						TextView.BufferType.SPANNABLE);
				parent.addView(valueTV);

			}
		}

	}

	public static String bbcode(String text) {
		return text;
		// String html = text;
		//
		// Map<String, String> bbMap = new HashMap<String, String>();
		//
		// bbMap.put("(\r\n|\r|\n|\n\r)", "<br/>");
		// bbMap.put("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
		// bbMap.put("\\[i\\](.+?)\\[/i\\]",
		// "<span style='font-style:italic;'>$1</span>");
		// bbMap.put("\\[u\\](.+?)\\[/u\\]",
		// "<span style='text-decoration:underline;'>$1</span>");
		// bbMap.put("\\[h1\\](.+?)\\[/h1\\]", "<h1>$1</h1>");
		// bbMap.put("\\[hr\\]", "<hr>");
		// bbMap.put("\\[h2\\](.+?)\\[/h2\\]", "<h2>$1</h2>");
		// bbMap.put("\\[h3\\](.+?)\\[/h3\\]", "<h3>$1</h3>");
		// bbMap.put("\\[h4\\](.+?)\\[/h4\\]", "<h4>$1</h4>");
		// bbMap.put("\\[h5\\](.+?)\\[/h5\\]", "<h5>$1</h5>");
		// bbMap.put("\\[h6\\](.+?)\\[/h6\\]", "<h6>$1</h6>");
		// bbMap.put("\\[quote\\](.+?)\\[/quote\\]",
		// "<blockquote>$1</blockquote>");
		// bbMap.put("\\[p\\](.+?)\\[/p\\]", "<p>$1</p>");
		// bbMap.put("\\[p=(.+?),(.+?)\\](.+?)\\[/p\\]",
		// "<p style='text-indent:$1px;line-height:$2%;'>$3</p>");
		// bbMap.put("\\[center\\](.+?)\\[/center\\]",
		// "<div align='center'>$1");
		// bbMap.put("\\[align=(.+?)\\](.+?)\\[/align\\]",
		// "<div align='$1'>$2");
		// bbMap.put("\\[color=(.+?)\\](.+?)\\[/color\\]",
		// "<span style='color:$1;'>$2</span>");
		// bbMap.put("\\[size=(.+?)\\](.+?)\\[/size\\]",
		// "<span style='font-size:$1;'>$2</span>");
		// bbMap.put("\\[img\\](.+?)\\[/img\\]", "<img src='$1' />");
		// bbMap.put("\\[img=(.+?),(.+?)\\](.+?)\\[/img\\]",
		// "<img width='$1' height='$2' src='$3' />");
		// bbMap.put("\\[email\\](.+?)\\[/email\\]",
		// "<a href='mailto:$1'>$1</a>");
		// bbMap.put("\\[email=(.+?)\\](.+?)\\[/email\\]",
		// "<a href='mailto:$1'>$2</a>");
		// bbMap.put("\\[url\\](.+?)\\[/url\\]", "<a href='$1'>$1</a>");
		// bbMap.put("\\[url=(.+?)\\](.+?)\\[/url\\]", "<a href='$1'>$2</a>");
		// bbMap.put(
		// "\\[youtube\\](.+?)\\[/youtube\\]",
		// "<object width='640' height='380'><param name='movie' value='http://www.youtube.com/v/$1'></param><embed src='http://www.youtube.com/v/$1' type='application/x-shockwave-flash' width='640' height='380'></embed></object>");
		// bbMap.put("\\[video\\](.+?)\\[/video\\]", "<video src='$1' />");
		//
		// // http
		// // bbMap.put("http://(.+?)", "<a href='$1'>$1</a>");
		//
		// for (Map.Entry entry : bbMap.entrySet()) {
		// html = html.replaceAll(entry.getKey().toString(), entry.getValue()
		// .toString());
		// }
		//
		// return html;
	}

	public static String htmlToBBCode(String text) {
		return text;
		// String html = text;
		//
		// Map<String, String> bbMap = new HashMap<String, String>();
		//
		// bbMap.put("<br/>", "\n");
		// bbMap.put("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
		// bbMap.put("\\[i\\](.+?)\\[/i\\]",
		// "<span style='font-style:italic;'>$1</span>");
		// bbMap.put("\\[u\\](.+?)\\[/u\\]",
		// "<span style='text-decoration:underline;'>$1</span>");
		// bbMap.put("\\[h1\\](.+?)\\[/h1\\]", "<h1>$1</h1>");
		// bbMap.put("\\[hr\\]", "<hr>");
		// bbMap.put("\\[h2\\](.+?)\\[/h2\\]", "<h2>$1</h2>");
		// bbMap.put("\\[h3\\](.+?)\\[/h3\\]", "<h3>$1</h3>");
		// bbMap.put("\\[h4\\](.+?)\\[/h4\\]", "<h4>$1</h4>");
		// bbMap.put("\\[h5\\](.+?)\\[/h5\\]", "<h5>$1</h5>");
		// bbMap.put("\\[h6\\](.+?)\\[/h6\\]", "<h6>$1</h6>");
		// bbMap.put("\\[quote\\](.+?)\\[/quote\\]",
		// "<blockquote>$1</blockquote>");
		// bbMap.put("\\[p\\](.+?)\\[/p\\]", "<p>$1</p>");
		// bbMap.put("\\[p=(.+?),(.+?)\\](.+?)\\[/p\\]",
		// "<p style='text-indent:$1px;line-height:$2%;'>$3</p>");
		// bbMap.put("\\[center\\](.+?)\\[/center\\]",
		// "<div align='center'>$1");
		// bbMap.put("\\[align=(.+?)\\](.+?)\\[/align\\]",
		// "<div align='$1'>$2");
		// bbMap.put("\\[color=(.+?)\\](.+?)\\[/color\\]",
		// "<span style='color:$1;'>$2</span>");
		// bbMap.put("\\[size=(.+?)\\](.+?)\\[/size\\]",
		// "<span style='font-size:$1;'>$2</span>");
		// bbMap.put("\\[img\\](.+?)\\[/img\\]", "<img src='$1' />");
		// bbMap.put("(<img\\b[^>]*\\bsrc\\s*=\\s*)([\"\'])((?:(?!\\2)[^>])*)\\2(\\s*[^>]*>)",
		// "[img]$1[/img]");
		// bbMap.put("\\[img=(.+?),(.+?)\\](.+?)\\[/img\\]",
		// "<img width='$1' height='$2' src='$3' />");
		// bbMap.put("\\[email\\](.+?)\\[/email\\]",
		// "<a href='mailto:$1'>$1</a>");
		// bbMap.put("\\[email=(.+?)\\](.+?)\\[/email\\]",
		// "<a href='mailto:$1'>$2</a>");
		// bbMap.put("\\[url\\](.+?)\\[/url\\]", "<a href='$1'>$1</a>");
		// bbMap.put("\\[url=(.+?)\\](.+?)\\[/url\\]", "<a href='$1'>$2</a>");
		// bbMap.put(
		// "\\[youtube\\](.+?)\\[/youtube\\]",
		// "<object width='640' height='380'><param name='movie' value='http://www.youtube.com/v/$1'></param><embed src='http://www.youtube.com/v/$1' type='application/x-shockwave-flash' width='640' height='380'></embed></object>");
		// bbMap.put("\\[video\\](.+?)\\[/video\\]", "<video src='$1' />");

		// http
		// bbMap.put("http://(.+?)", "<a href='$1'>$1</a>");

		// for (Map.Entry entry : bbMap.entrySet()) {
		// html = html.replaceAll(entry.getKey().toString(), entry.getValue()
		// .toString());
		// }
		//
		// return html;
	}
}