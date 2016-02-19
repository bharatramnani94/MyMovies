package com.bharatramnani.mymovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.bharatramnani.mymovies.data.MovieContract;
import com.bharatramnani.mymovies.data.MovieDbHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.
        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MoviesEntry.TABLE_NAME);
//        tableNameHashSet.add(MovieContract.ReviewEntry.TABLE_NAME);
//        tableNameHashSet.add(MovieContract.TrailerEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the tables",
                tableNameHashSet.isEmpty());

        // TESTING MOVIES TABLE
        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MoviesEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> moviesColumnHashSet = new HashSet<String>();
        moviesColumnHashSet.add(MovieContract.MoviesEntry._ID);
        moviesColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_OVERVIEW);
        moviesColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_POSTER);
        moviesColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE);
        moviesColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_TITLE);
        moviesColumnHashSet.add(MovieContract.MoviesEntry.COLUMN_VOTE_AVG);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            moviesColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                moviesColumnHashSet.isEmpty());


        // TESTING REVIEWS TABLE
        // now, do our reviews table contain the correct columns?
//        c = db.rawQuery("PRAGMA table_info(" + MovieContract.ReviewEntry.TABLE_NAME + ")",
//                null);

//        assertTrue("Error: This means that we were unable to query the database for table information.",
//                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
//        final HashSet<String> reviewsColumnHashSet = new HashSet<String>();
//        reviewsColumnHashSet.add(MoviesContract.ReviewEntry._ID);
//        reviewsColumnHashSet.add(MoviesContract.ReviewEntry.COLUMN_MOVIE_ID);
//        reviewsColumnHashSet.add(MoviesContract.ReviewEntry.COLUMN_REVIEW_AUTHOR);
//        reviewsColumnHashSet.add(MoviesContract.ReviewEntry.COLUMN_REVIEW_CONTENT);
//
//        columnNameIndex = c.getColumnIndex("name");
//        do {
//            String columnName = c.getString(columnNameIndex);
//            reviewsColumnHashSet.remove(columnName);
//        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
//        assertTrue("Error: The database doesn't contain all of the required location entry columns",
//                reviewsColumnHashSet.isEmpty());


        // TESTING TRAILERS TABLE
        // now, do our reviews table contain the correct columns?
//        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.TrailerEntry.TABLE_NAME + ")",
//                null);
//
//        assertTrue("Error: This means that we were unable to query the database for table information.",
//                c.moveToFirst());
//
//         Build a HashSet of all of the column names we want to look for
//        final HashSet<String> trailersColumnHashSet = new HashSet<String>();
//        trailersColumnHashSet.add(MoviesContract.TrailerEntry._ID);
//        trailersColumnHashSet.add(MoviesContract.TrailerEntry.COLUMN_MOVIE_ID);
//        trailersColumnHashSet.add(MoviesContract.TrailerEntry.COLUMN_TRAILER_NAME);
//        trailersColumnHashSet.add(MoviesContract.TrailerEntry.COLUMN_TRAILER_THUMBNAIL);
//        trailersColumnHashSet.add(MoviesContract.TrailerEntry.COLUMN_TRAILER_URL);
//
//        columnNameIndex = c.getColumnIndex("name");
//        do {
//            String columnName = c.getString(columnNameIndex);
//            trailersColumnHashSet.remove(columnName);
//        } while(c.moveToNext());
//
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
//        assertTrue("Error: The database doesn't contain all of the required location entry columns",
//                trailersColumnHashSet.isEmpty());
//

        db.close();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        location database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createNorthPoleLocationValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public void testLocationTable() {
        // First step: Get reference to writable database
        MovieDbHelper dbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MoviesEntry._ID, 1234);
        testValues.put(MovieContract.MoviesEntry.COLUMN_TITLE, "Test Movie Title");
        testValues.put(MovieContract.MoviesEntry.COLUMN_OVERVIEW, "Test Movie Overview");
        testValues.put(MovieContract.MoviesEntry.COLUMN_POSTER, "Test Movie Poster URL");
        testValues.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, "21-12-2017");
        testValues.put(MovieContract.MoviesEntry.COLUMN_VOTE_AVG, 7.8);

        // Insert ContentValues into database and get a row ID back
        long rowId = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, testValues);
        assertTrue(rowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(MovieContract.MoviesEntry.TABLE_NAME,null,null,null,null,null,null);

        // Move the cursor to a valid database row
        assertTrue("Error: No records returned.", cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        validateCurrentRecord("Error: Movie query validation failed.", cursor, testValues);

        // Assert that only one row exists.
        assertFalse("Error: More than one rows entered.", cursor.moveToNext());

        // Finally, close the cursor and database
        cursor.close();
        db.close();

    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testWeatherTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testLocationTable
        // we can move this code to insertLocation and then call insertLocation from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testLocationTable can only return void because it's a test.

        // First step: Get reference to writable database

        // Create ContentValues of what you want to insert
        // (you can use the createWeatherValues TestUtilities function if you wish)

        // Insert ContentValues into database and get a row ID back

        // Query the database and receive a Cursor back

        // Move the cursor to a valid database row

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)

        // Finally, close the cursor and database
    }


    /*
        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.
     */
    public long insertLocation() {
        return -1L;
    }


    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

}