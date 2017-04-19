package com.nothio.bucher.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nothio.bucher.R;

public class DatabaseInitializer extends SQLiteOpenHelper{
	
    private static String DB_PATH = "/data/data/com.nothio.bucher/databases/";
    private static String DB_NAME = "section.db";
 
    private SQLiteDatabase database; 
    private final Context context;
    private boolean ForceCopy = true;
    
    
    public DatabaseInitializer(Context context) {
    	super(context, DB_NAME, null, 2);
        this.context = context;
    }	
    
    public void createDatabase() throws IOException{
    	
    	boolean dbExist = checkDataBase();
  
    	
    

    		
    	if(!ForceCopy & dbExist){
    		Log.w("sadegh", "db exist !!!");
    	}else{
    		Log.w("sadegh", "lets copy db :D");
    			copyDataBase();
    	}
 
    }
   
    
    /**
   * Check if the database already exist to avoid re-copying the file each time you open the application.
   * @return true if it exists, false if it doesn't
   */
  private boolean checkDataBase(){

  	SQLiteDatabase checkDB = null;

  	try{
  		String myPath = DB_PATH + DB_NAME;
  		
  		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

  	}catch(SQLiteException e){

  		//database does't exist yet.

  	}

  	if(checkDB != null){

  		checkDB.close();

  	}

  	return checkDB != null ? true : false;
  }

  /**
   * Copies your database from your local assets-folder to the just created empty database in the
   * system folder, from where it can be accessed and handled.
   * This is done by transfering bytestream.
   * */
  private void copyDataBase() throws IOException{
	  
	 this.getReadableDatabase();
		this.close();
	  
  	//Open your local db as the input stream
  	InputStream myInput = this.context.getResources().openRawResource(R.raw.section);

  	
  	// Path to the just created empty db
  	String outFileName = DB_PATH + DB_NAME;

  	//Open the empty db as the output stream
  	OutputStream myOutput = new FileOutputStream(outFileName);

  	//transfer bytes from the inputfile to the outputfile
  	byte[] buffer = new byte[1024];
  	int length;
  	while ((length = myInput.read(buffer))>0){
  		myOutput.write(buffer, 0, length);
  	}

  	//Close the streams
  	myOutput.flush();
  	myOutput.close();
  	myInput.close();

  }

    
    
    
 
    @Override
	public synchronized void close() {
	    if(database != null)
		    database.close();
	    
	    super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
 
}
