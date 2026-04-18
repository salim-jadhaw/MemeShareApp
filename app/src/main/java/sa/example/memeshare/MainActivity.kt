package sa.example.memeshare
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    // reffernce all like image , prog_baar , share , next
    private lateinit var memeImageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var nextButton: Button
    private lateinit var shareButton: Button
    private var currentMemeUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)        

        // accessing id of all ui components from XML
        memeImageView = findViewById(R.id.memeImageView)
        progressBar = findViewById(R.id.progressBar)
        nextButton = findViewById(R.id.nextButton)
        shareButton = findViewById(R.id.shareButton)
        // on the call of Oncreate method first load a meme
        loadMeme()

        // will click on next btn then again new meme will display
        nextButton.setOnClickListener {
            loadMeme()
        }

        // sharing the images/meme through all supported application in phone
        shareButton.setOnClickListener {
            shareMeme()
        }
    }

    private fun loadMeme() {
        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentMemeUrl = response.getString("url")
                Glide.with(this)
                    .load(currentMemeUrl)
                    .into(memeImageView)
                progressBar.visibility = View.GONE
            },
            {
                Toast.makeText(this, "Failed to load meme!", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun shareMeme() {
        if (currentMemeUrl != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Check out this meme: $currentMemeUrl")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share this meme via:"))
        } else {
            Toast.makeText(this, "Meme not loaded yet!", Toast.LENGTH_SHORT).show()
        }
    }
}
