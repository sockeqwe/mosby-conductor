package com.hannesdorfmann.mosby3.conductor.sample.create

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotterknife.bindView
import com.hannesdorfmann.adapterdelegates2.AbsListItemAdapterDelegate
import com.hannesdorfmann.mosby3.conductor.sample.R.color
import com.hannesdorfmann.mosby3.conductor.sample.R.id
import com.hannesdorfmann.mosby3.conductor.sample.R.layout
import com.hannesdorfmann.mosby3.conductor.sample.create.ImageAdapterDelegate.ImageViewHolder
import com.squareup.picasso.Picasso

/**
 * Displays an image in recyclerview
 *
 * @author Hannes Dorfmann
 */
class ImageAdapterDelegate(private val picasso: Picasso, private val layoutInflater: LayoutInflater, private val clickListener: (Uri) -> Unit) : AbsListItemAdapterDelegate<Uri, Uri, ImageViewHolder>() {

  override fun onBindViewHolder(item: Uri, viewHolder: ImageViewHolder) {
    picasso.load(item)
        .placeholder(color.imagePlaceholder)
        .fit()
        .centerCrop()
        .into(viewHolder.imageView)

    viewHolder.uri = item
  }

  override fun isForViewType(item: Uri, items: MutableList<Uri>?, position: Int): Boolean
      = item is Uri

  override fun onCreateViewHolder(parent: ViewGroup): ImageViewHolder
      = ImageViewHolder(layoutInflater.inflate(layout.item_image, parent, false), clickListener)

  class ImageViewHolder(view: View, private val clickListener: (Uri) -> Unit) : RecyclerView.ViewHolder(
      view) {
    val imageView by bindView<ImageView>(id.imagePreview)
    lateinit var uri: Uri

    init {
      imageView.setOnClickListener { clickListener(uri) }
    }
  }
}