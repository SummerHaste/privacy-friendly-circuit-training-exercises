/**
 * This file is part of Privacy Friendly Circuit Trainer.
 * Privacy Friendly Circuit Trainer is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or any later version.
 * Privacy Friendly Circuit Trainer is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Privacy Friendly Interval Timer. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.UnoVivo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.secuso.UnoVivo.R;
import org.secuso.UnoVivo.database.PFASQLiteHelper;
import org.secuso.UnoVivo.models.Exercise;
import org.secuso.UnoVivo.models.ExerciseSet;
import org.secuso.UnoVivo.tutorial.PrefManager;
import org.secuso.UnoVivo.tutorial.TutorialActivity;

import java.util.ArrayList;

/**
 * @author Karola Marky, Nils Schroth
 * @version 20180321
 */


public class SplashActivity extends AppCompatActivity {

    private final PFASQLiteHelper db = new PFASQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefManager.performMigrations(getBaseContext());
        if (PrefManager.isFirstTimeLaunch(getBaseContext())) {
            //add two example exercises

            Uri ic_squat = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.drawable.ic_exercise_squat);
            Uri ic_pushup = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.drawable.ic_exercise_pushup);
            Exercise defaultExercise1 = new Exercise(0, "Squat", "Example description", ic_squat);
            Exercise defaultExercise2 = new Exercise(0, "Pushup", "Example description", ic_pushup);
            db.addExercise(defaultExercise1);
            db.addExercise(defaultExercise2);
            ArrayList<Integer> tmp = new ArrayList<>();
            tmp.add(db.getAllExercise().get(0).getID());
            tmp.add(db.getAllExercise().get(1).getID());
            ExerciseSet defaultExerciseSet = new ExerciseSet(0, "Example", tmp);

            db.addExerciseSet(defaultExerciseSet);
            Intent mainIntent = new Intent(SplashActivity.this, TutorialActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        } else {
            //Update icons for default exercises
            Exercise defaultExercise1 = db.getExercise(1);
            Exercise defaultExercise2 = db.getExercise(2);
            if (defaultExercise1 != null && defaultExercise1.getImage() != null && defaultExercise1.getImage().toString().startsWith("android.resource://" + this.getPackageName() + "/")) {
                defaultExercise1.setImage(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.drawable.ic_exercise_squat));
                db.updateExercise(defaultExercise1);
            }
            if (defaultExercise2 != null && defaultExercise2.getImage() != null && defaultExercise2.getImage().toString().startsWith("android.resource://" + this.getPackageName() + "/")) {
                defaultExercise2.setImage(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.drawable.ic_exercise_pushup));
                db.updateExercise(defaultExercise2);
            }


            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }
    }
}
