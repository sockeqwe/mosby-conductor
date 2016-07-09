package com.hannesdorfmann.mosby.conductor.sample.create

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.bindView
import com.hannesdorfmann.adapterdelegates2.AbsListItemAdapterDelegate
import com.hannesdorfmann.mosby.conductor.sample.R
import com.squareup.picasso.Picasso

/**
 * Displays an image in recyclerview
 *
 * @author Hannes Dorfmann
 */
class ImageAdapterDelegate(private val picasso: Picasso, private val layoutInflater: LayoutInflater, private val clickListener: (Uri) -> Unit) : AbsListItemAdapterDelegate<Uri, Uri, ImageAdapterDelegate.ImageViewHolder>() {

  override fun onBindViewHolder(item: Uri, viewHolder: ImageViewHolder) {
    picasso.load(item)
        .placeholder(R.color.imagePlaceholder)
        .fit()
        .centerCrop()
        .into(viewHolder.imageView)

    viewHolder.uri = item
  }

  override fun isForViewType(item: Uri, items: MutableList<Uri>?, position: Int): Boolean
      = item is Uri

  override fun onCreateViewHolder(parent: ViewGroup): ImageViewHolder
      = ImageViewHolder(layoutInflater.inflate(R.layout.item_image, parent, false), clickListener)

  class ImageViewHolder(view: View, private val clickListener: (Uri) -> Unit) : RecyclerView.ViewHolder(
      view) {
    val imageView by bindView<ImageView>(R.id.imagePreview)
    lateinit var uri: Uri

    init {
      imageView.setOnClickListener { clickListener(uri) }
    }
  }
}