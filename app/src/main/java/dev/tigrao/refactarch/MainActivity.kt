package dev.tigrao.refactarch

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val api = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(Api::class.java)

        val recycler = findViewById<RecyclerView>(R.id.recycler)


        val asyncTask = object : AsyncTask<String, Unit, BreedResponse>() {
            override fun doInBackground(vararg params: String?): BreedResponse {
                return runBlocking {
                    api.fetchByBreed(params!!.first()!!)
                }
            }

            override fun onPostExecute(result: BreedResponse?) {
                super.onPostExecute(result)

                recycler.adapter = DogAdapter(result!!.message!!)
                recycler.layoutManager = GridLayoutManager(this@MainActivity, 3)
            }
        }

        var race = ""

        repeat(100) { position ->
            race = if (position % 2 == 0) {
                "african"
            } else if (position > 90) {
                "boxer"
            } else {
                "beagle"
            }
        }

        asyncTask.execute(race)
    }

    class DogAdapter(val dogs: List<String>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_dog, parent, false)

            return object : ViewHolder(view) {

            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val image = holder.itemView.findViewById<ImageView>(R.id.image)

            Glide.with(image)
                .load(dogs[position])
                .centerCrop()
                .into(image)
        }

        override fun getItemCount(): Int = dogs.size

    }
}