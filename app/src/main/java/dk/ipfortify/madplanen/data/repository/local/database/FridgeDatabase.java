package dk.ipfortify.madplanen.data.repository.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;
import dk.ipfortify.madplanen.data.repository.local.dao.FridgeLocalDataSource;

@Database(entities = {RecipeIngredient.class}, version = 1, exportSchema = false)
public abstract class FridgeDatabase extends RoomDatabase {


    private static volatile FridgeDatabase INSTANCE;

    public abstract FridgeLocalDataSource dao();
    private static final String DATABASE_NAME = "fridge.db";

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FridgeDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FridgeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            FridgeDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
//https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
}
