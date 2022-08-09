package com.blez.memelord

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import coil.imageLoader
import coil.request.ImageRequest
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.blez.memelord.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        lifecycleScope.launch {
            loadData(null)
        }
       binding.nextbtn.setOnClickListener {
           lifecycleScope.launch {
               loadData(null)
           }
       }

    }

    private suspend  fun loadData(subreddit : String?)
    {
        binding.circularprogress.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
               val imageURL =  response.getString("url")
                Glide.with(this).load(imageURL).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                    binding.circularprogress.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.circularprogress.visibility = View.GONE
                        return false
                    }
                }).into(binding.memeImageView)
                binding.TitleTextView.text =  response.getString("title").toString()
               val author =  response.getString("author").toString()
                binding.AuthorTextView.text = "Author : $author"
                val subreddit = response.getString("subreddit").toString()
                binding.openredditbtn.text =  "r/${subreddit}"
                binding.ups.text = "${response.getString("ups")}👍"

            },
            { error ->
               binding.TitleTextView.text = error.toString()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);



    }
    private fun filer(subreddit : String){

    }
}