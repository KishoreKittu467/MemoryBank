package com.kkapps.memorybank.commons.adapters

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.kkapps.memorybank.R
import com.kkapps.memorybank.commons.activities.BaseSimpleActivity
import com.kkapps.memorybank.commons.extensions.*
import com.kkapps.memorybank.commons.helpers.getFilePlaceholderDrawables
import com.kkapps.memorybank.commons.models.FileDirItem
import com.kkapps.memorybank.commons.views.MyRecyclerView
import kotlinx.android.synthetic.main.filepicker_list_item.view.*
import java.util.*

class FilepickerItemsAdapter(activity: BaseSimpleActivity, val fileDirItems: List<FileDirItem>, recyclerView: MyRecyclerView,
                             itemClick: (Any) -> Unit) : MyRecyclerViewAdapter(activity, recyclerView, null, itemClick) {

    private lateinit var fileDrawable: Drawable
    private lateinit var folderDrawable: Drawable
    private var fileDrawables = HashMap<String, Drawable>()
    private val hasOTGConnected = activity.hasOTGConnected()
    private var fontSize = 0f

    init {
        initDrawables()
        fontSize = activity.getTextSize()
    }

    override fun getActionMenuId() = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = createViewHolder(R.layout.filepicker_list_item, parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fileDirItem = fileDirItems[position]
        holder.bindView(fileDirItem, true, false) { itemView, adapterPosition ->
            setupView(itemView, fileDirItem)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = fileDirItems.size

    override fun prepareActionMode(menu: Menu) {}

    override fun actionItemPressed(id: Int) {}

    override fun getSelectableItemCount() = fileDirItems.size

    override fun getIsItemSelectable(position: Int) = false

    override fun getItemKeyPosition(key: Int) = fileDirItems.indexOfFirst { it.path.hashCode() == key }

    override fun getItemSelectionKey(position: Int) = fileDirItems[position].path.hashCode()

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (!activity.isDestroyed && !activity.isFinishing) {
            Glide.with(activity).clear(holder.itemView.list_item_icon!!)
        }
    }

    private fun setupView(view: View, fileDirItem: FileDirItem) {
        view.apply {
            list_item_name.text = fileDirItem.name
            list_item_name.setTextColor(textColor)
            list_item_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

            list_item_details.setTextColor(textColor)
            list_item_details.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

            if (fileDirItem.isDirectory) {
                list_item_icon.setImageDrawable(folderDrawable)
                list_item_details.text = getChildrenCnt(fileDirItem)
            } else {
                list_item_details.text = fileDirItem.size.formatSize()
                val path = fileDirItem.path
                val placeholder = fileDrawables.getOrElse(fileDirItem.name.substringAfterLast(".").toLowerCase(Locale.getDefault()), { fileDrawable })
                val options = RequestOptions()
                        .centerCrop()
                        .error(placeholder)

                var itemToLoad = if (fileDirItem.name.endsWith(".apk", true)) {
                    val packageInfo = context.packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
                    if (packageInfo != null) {
                        val appInfo = packageInfo.applicationInfo
                        appInfo.sourceDir = path
                        appInfo.publicSourceDir = path
                        appInfo.loadIcon(context.packageManager)
                    } else {
                        path
                    }
                } else {
                    path
                }

                if (!activity.isDestroyed && !activity.isFinishing) {
                    if (itemToLoad.toString().isGif()) {
                        Glide.with(activity).asBitmap().load(itemToLoad).apply(options).into(list_item_icon)
                    } else {
                        if (hasOTGConnected && itemToLoad is String && activity.isPathOnOTG(itemToLoad)) {
                            itemToLoad = itemToLoad.getOTGPublicPath(activity)
                        }

                        Glide.with(activity)
                                .load(itemToLoad)
                                .transition(withCrossFade())
                                .apply(options)
                                .into(list_item_icon)
                    }
                }
            }
        }
    }

    private fun getChildrenCnt(item: FileDirItem): String {
        val children = item.children
        return activity.resources.getQuantityString(R.plurals.items, children, children)
    }

    private fun initDrawables() {
        folderDrawable = resources.getColoredDrawableWithColor(R.drawable.ic_folder_vector, textColor)
        folderDrawable.alpha = 180
        fileDrawable = resources.getDrawable(R.drawable.ic_file_generic)
        fileDrawables = getFilePlaceholderDrawables(activity)
    }
}
