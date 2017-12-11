package ar.sample.blanco.com.samplear;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.craftar.CraftARError;
import com.craftar.CraftAROnDeviceCollection;
import com.craftar.CraftAROnDeviceCollectionManager;
import com.craftar.CraftARSDK;

/**
 * Created by ian.blanco on 12/11/2017.
 */

public class SplashActivity extends Activity implements CraftAROnDeviceCollectionManager.AddCollectionListener {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        CraftARSDK.Instance().init(getApplicationContext());

        /** We will load the collection for On Device AR ( 4th example).
         * If you use Cloud AR, you don't need to add the collection*/

        CraftAROnDeviceCollectionManager collectionManager = CraftAROnDeviceCollectionManager.Instance();

        /**
         * The on-device collection may already be added to the device (we just add it once)
         * we can use the token to retrieve it.
         */
        CraftAROnDeviceCollection collection = collectionManager.get(Config.MY_COLLECTION_TOKEN);
        if(collection != null){
            Log.d(this.getClass().getSimpleName(), "Collection already added, starting launchers activity!");
//            startLaunchersActivity();

        }else{
            /**
             * If not, we get the path for the bundle and add the collection to the device first.
             * The addCollection  method receives an AddCollectionListener instance that will receive
             * the callbacks when the collection is ready.
             */
            Log.d(this.getClass().getSimpleName(), "Collection NOT added, adding collection");
            collectionManager.addCollection("arbundle.zip", this);
//			collectionManager.addCollection("bundle_two", this);
            //Alternatively, you can also download the collection from CraftAR using the token, instead of embedding it into the app resources.
            //collectionManager.addCollectionWithToken(TOKEN, this);
        }
    }

    @Override
    public void collectionAdded(CraftAROnDeviceCollection craftAROnDeviceCollection) {

    }

    @Override
    public void addCollectionFailed(CraftARError craftARError) {

    }

    @Override
    public void addCollectionProgress(float v) {

    }
}
