package com.wedcel.adaptive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView text;
	private String title;
	private ProgressBar down_progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//text = (TextView)findViewById(R.id.text);
		down_progress = (ProgressBar)findViewById(R.id.down_progress);
		title = "测试android不同客户端的适配";
		//text.setText(title);
		RefreshAsyn ansy = new RefreshAsyn(this);
		ansy.execute("");
		System.out.println("MainActivity onCreate");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("MainActivity onDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("MainActivity onPause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("MainActivity onResume");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("MainActivity onStop");
	}

	/*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation 
                == Configuration.ORIENTATION_LANDSCAPE) {
            //当前为横屏， 在此处添加额外的处理代码
        	System.out.println("横屏");
        }
        else if (this.getResources().getConfiguration().orientation 
                == Configuration.ORIENTATION_PORTRAIT) {
            //当前为竖屏， 在此处添加额外的处理代码
        	
        	System.out.println("竖屏");
        }
        //检测实体键盘的状态：推出或者合上    
        if (newConfig.hardKeyboardHidden 
                == Configuration.HARDKEYBOARDHIDDEN_NO){ 
            //实体键盘处于推出状态，在此处添加额外的处理代码
        	
        	System.out.println("键盘推出");
        } 
        else if (newConfig.hardKeyboardHidden
                == Configuration.HARDKEYBOARDHIDDEN_YES){ 
            //实体键盘处于合上状态，在此处添加额外的处理代码
        	System.out.println("键盘合上");
        }
        
    }*/
	
	
	public class RefreshAsyn extends AsyncTask<String, Integer, String> {
		//private ProgressDialog progressDialog;
		private int fileSize = 0;
		private Context mContext;
		
		public RefreshAsyn( Context context) {
			super();
			mContext = context;
			 
			
		}

		@Override
		protected String doInBackground(String... arg0) {
			
			// 下载更新包
			try {
				down();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
				int downLoadFileSize = values[0];
				System.out.println("downLoadFileSize:"+downLoadFileSize+"fileSize"+fileSize+"%:"+( (int) (downLoadFileSize*1.0/fileSize* 100)));
				down_progress.setProgress( (int) (downLoadFileSize*1.0/fileSize* 100));
		}

		/**
		 * 和后台交互之前
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		/**
		 * 后台交互之后，更新界面
		 */
		@Override
		protected void onPostExecute(String result) {
				// 下载更新包成功
		 
			Intent intent = new Intent(MainActivity.this,NextActivity.class);
			startActivity(intent);
				
		}

		private void down() throws Exception {

			FileOutputStream out = null;
			OutputStreamWriter writer = null;
			int downLoadFileSize;
			try {
				
				String url = "http://file1.henhaozhuan.com/upload/files/48452901afdca514/AnZhiV4.4.1.apk";
				URLConnection localURLConnection = new URL(url).openConnection();
				localURLConnection.connect();
				InputStream is = localURLConnection.getInputStream();
				fileSize = localURLConnection.getContentLength();
				if (fileSize <= 0)
					throw new RuntimeException("无法获知文件大小 ");
				if (is == null)
					throw new RuntimeException("stream is null");
				File basePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"ttaxy.apk");
				if (!basePath.exists()) {
					basePath.createNewFile();
				}
			 

				FileOutputStream fos = new FileOutputStream(basePath);
				// 把数据存入路径+文件名
				byte buf[] = new byte[1024 * 4];
				downLoadFileSize = 0;
				publishProgress(0, downLoadFileSize);// 更新进度条

				int readCount = 0;
				do {
					// 循环读取
					int numread = is.read(buf);
					if (numread == -1) {
						break;
					}
					fos.write(buf, 0, numread);
					downLoadFileSize += numread;
					if (readCount % 10 == 0) {
						publishProgress(downLoadFileSize);// 更新进度条
					}

					readCount++;
				} while (true);
				try {
					fos.close();
					is.close();
				} catch (Exception ex) {
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				try {
					if (null != out) {
						out.close();
					}
					if (null != writer) {
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
}
