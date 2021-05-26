package com.projectdelta.optimize.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.projectdelta.optimize.constant.DATABASE_NAME
import com.projectdelta.optimize.data.entities.Container
import com.projectdelta.optimize.data.entities.Project
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.util.Converters
import java.util.concurrent.Executors

@Database(
	entities = [
		Project::class ,
		Container::class ,
		Worker::class
	],
	version = 2,
	exportSchema = false
)

@TypeConverters( Converters::class )
abstract class ProjectDatabase : RoomDatabase() {

	abstract fun projectDao() : ProjectDao

	companion object{
		// For Singleton instantiation
		@Volatile
		private var INSTANCE: ProjectDatabase? = null

		fun getInstance(context: Context): ProjectDatabase {
			val tempInstance = INSTANCE
			if( tempInstance != null )
				return tempInstance

			synchronized(this){
				val instance = buildDatabase(context)
				INSTANCE = instance
				return instance
			}
		}

		private fun buildDatabase( context: Context ) : ProjectDatabase {
			return Room.databaseBuilder(
				context.applicationContext ,
				ProjectDatabase::class.java ,
				DATABASE_NAME
			).addCallback(object :  RoomDatabase.Callback() {
				override fun onCreate(db: SupportSQLiteDatabase){
					super.onCreate(db)
					// pre-populate data
					// prepopulate works on a new thread so can return Null if database is not build while required
					Executors.newSingleThreadExecutor().execute {
						INSTANCE?.let {
							// it.ProjectDao().insertData(createPrePopulateData())
						}
					}
				}
			}
			).fallbackToDestructiveMigration() .build()
		}
	}

}