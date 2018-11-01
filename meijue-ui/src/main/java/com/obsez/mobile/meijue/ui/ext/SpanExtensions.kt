@file:Suppress("NOTHING_TO_INLINE")

package com.obsez.mobile.meijue.ui.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.MaskFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleRes
import com.bumptech.glide.Glide


/**
 * Highlight substring [query] in this spannable with custom [style]. All occurrences of this substring
 * are stylized
 */
fun Spannable.highlightSubstring(query: String, @StyleRes style: Int): Spannable {
    val spannable = Spannable.Factory.getInstance().newSpannable(this)
    val substrings = query.toLowerCase().split("\\s+".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
    var lastIndex = 0
    for (i in substrings.indices) {
        do {
            lastIndex = this.toString().toLowerCase().indexOf(substrings[i], lastIndex)
            if (lastIndex != -1) {
                spannable.setSpan(StyleSpan(style), lastIndex, lastIndex + substrings[i].length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                lastIndex++
            }
        } while (lastIndex != -1)
    }
    return spannable
}


/**
 * Kotlin wrapper around SpannableStringBuilder
 *
 * Inspired by JakeWharton's Truss and Kotlin's kotlinx.html
 *
 * @see <a href="https://gist.github.com/JakeWharton/11274467">Truss</a>
 * @see <a href="https://github.com/Kotlin/kotlinx.html">kotlinx.html</a>
 *
 * https://github.com/SimonMarquis/Android-Spans
 *
 * Sample:
 * <code>val span = span {
 *   +"Spans "
 *   bold {
 *     +"are "
 *     strikeThrough { +"hard?" }
 *   }
 * }
 * textView.text = span.build()
 * </code>
 *
 * And:
 * <code>
 * </code>
 *
 *
 *
 *
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
open class TextSpan {

    private val spans = ArrayList<TextSpan>()

    open fun build(builder: SpannableStringBuilder = SpannableStringBuilder()): Spannable {
        spans.forEach { span -> span.build(builder) }
        return builder
    }
    
    open fun toTextView(tv: TextView, builder: SpannableStringBuilder = SpannableStringBuilder()): Spannable {
        spans.forEach { span -> span.build(builder) }
        tv.text = builder
        tv.movementMethod = LinkMovementMethod.getInstance()
        return builder
    }
    
    open fun toCharSequence(): CharSequence = build()


    fun span(what: Any, init: (Node.() -> Unit)? = null): TextSpan {
        val child = Node(what)
        if (init != null) {
            child.init()
        }
        spans.add(child)
        return this
    }

    fun text(content: String): TextSpan {
        spans.add(Leaf(content))
        return this
    }

    operator fun String.unaryPlus() = text(this)
    operator fun TextSpan.unaryPlus() = this


    operator fun plus(nextValue: Any) : TextSpan {
        if(nextValue is TextSpan)
            spans.addAll(nextValue.spans)
        else
            spans.add(Node(nextValue))
        return this
    }


    fun style(style: Int, span: StyleSpan = StyleSpan(style), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun normal(span: StyleSpan = StyleSpan(Typeface.NORMAL), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun bold(span: StyleSpan = StyleSpan(Typeface.BOLD), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun italic(span: StyleSpan = StyleSpan(Typeface.ITALIC), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun boldItalic(span: StyleSpan = StyleSpan(Typeface.BOLD_ITALIC), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun underline(span: UnderlineSpan = UnderlineSpan(), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun typeface(family: String, span: TypefaceSpan = TypefaceSpan(family), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun sansSerif(span: TypefaceSpan = TypefaceSpan("sans-serif"), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun serif(span: TypefaceSpan = TypefaceSpan("serif"), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun monospace(span: TypefaceSpan = TypefaceSpan("monospace"), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun appearance(context: Context, appearance: Int, span: TextAppearanceSpan = TextAppearanceSpan(context, appearance), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun superScript(span: SuperscriptSpan = SuperscriptSpan(), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun subScript(span: SubscriptSpan = SubscriptSpan(), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun strikeThrough(span: StrikethroughSpan = StrikethroughSpan(), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun scaleX(proportion: Float, span: ScaleXSpan = ScaleXSpan(proportion), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun relativeSize(proportion: Float, span: RelativeSizeSpan = RelativeSizeSpan(proportion), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun absoluteSize(size: Int, dip: Boolean = false, span: AbsoluteSizeSpan = AbsoluteSizeSpan(size, dip), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun quote(color: Int = Color.BLACK, span: QuoteSpan = QuoteSpan(color), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun mask(filter: MaskFilter, span: MaskFilterSpan = MaskFilterSpan(filter), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun leadingMargin(every: Int = 0, span: LeadingMarginSpan = LeadingMarginSpan.Standard(every), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun leadingMargin(first: Int = 0, rest: Int = 0, span: LeadingMarginSpan = LeadingMarginSpan.Standard(first, rest), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun foregroundColor(color: Int = Color.BLACK, span: ForegroundColorSpan = ForegroundColorSpan(color), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun backgroundColor(color: Int = Color.BLACK, span: BackgroundColorSpan = BackgroundColorSpan(color), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun bullet(gapWidth: Int = 0, color: Int = Color.BLACK, span: BulletSpan = BulletSpan(gapWidth, color), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun align(align: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL, span: AlignmentSpan.Standard = AlignmentSpan.Standard(align), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun drawableMargin(drawable: Drawable, padding: Int = 0, span: DrawableMarginSpan = DrawableMarginSpan(drawable, padding), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun iconMargin(bitmap: Bitmap, padding: Int = 0, span: IconMarginSpan = IconMarginSpan(bitmap, padding), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun image(context: Context, bitmap: Bitmap, verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM, span: ImageSpan = ImageSpan(context, bitmap, verticalAlignment), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun image(drawable: Drawable, verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM, span: ImageSpan = ImageSpan(drawable, verticalAlignment), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun image(context: Context, uri: Uri, verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM, span: ImageSpan = ImageSpan(context, uri, verticalAlignment), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)
    fun image(context: Context, resourceId: Int, verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM, span: ImageSpan = ImageSpan(context, resourceId, verticalAlignment), init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun clickable(onClick: (ClickableSpan) -> Unit, span: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View?) {
            onClick(this)
        }
    }, init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun url(url: String, onClick: (URLSpan) -> Boolean, span: URLSpan = object : URLSpan(url) {
        override fun onClick(widget: View?) {
            if (onClick(this)) {
                super.onClick(widget!!)
            }
        }
    }, init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    /**
     * NOTE: you may make the TextView links clickable:
     *
     * textView1.movementMethod = LinkMovementMethod()
     * textView1.autoLinkMask = Linkify.ALL
     *
     * and add android:linksClickable="true"
     *
     * Or, use autoLink="web"
     * Or, use @{link #html(...)}
     *
     * See also: https://stackoverflow.com/questions/2734270/how-do-i-make-links-in-a-textview-clickable
     *
     */
    fun link(url: String, onClick: (URLSpan) -> Boolean, span: URLSpan = object : URLSpan(url) {
        override fun onClick(widget: View?) {
            if (onClick(this)) {
                super.onClick(widget!!)
            }
        }
    }, init: (TextSpan.() -> Unit)? = null): TextSpan = span(span, init)

    fun html(html: String, context: Context, imageGetter: Html.ImageGetter = imageGetter(context), init: (TextSpan.() -> Unit)? = null): TextSpan {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spans.add(Leaf(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)))
        } else {
            @Suppress("DEPRECATION")
            spans.add(Leaf(Html.fromHtml(html, imageGetter, null)))
        }

        if (init != null) {
            this.init()
        }
        return this
    }

    private fun imageGetter(context: Context): Html.ImageGetter {
        return Html.ImageGetter {
            Glide.with(context).load(it).submit(-1, -1).get()
        }
    }

    class Node(val span: Any) : TextSpan() {
        override fun build(builder: SpannableStringBuilder): Spannable {
            val start = builder.length
            super.build(builder)
            builder.setSpan(span, start, builder.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            return builder
        }
    }

    class Leaf(val content: Any) : TextSpan() {
        override fun build(builder: SpannableStringBuilder): Spannable {
            builder.append(content.toString())
            return builder
        }
    }

}

fun span(init: TextSpan.() -> Unit): TextSpan {
    val spanWithChildren = TextSpan()
    spanWithChildren.init()
    return spanWithChildren
}


//


inline fun String.span() = TextSpan().text(this)


fun TextView.setText(textSpan: TextSpan) {
    // val builder = StringBuilder()
    // textSpan.textConstructor.forEach {
    //     builder.append(it)
    // }
    //
    // val spanStr = SpannableString(builder.toString())
    // var index = 0
    // textSpan.textConstructor.forEachIndexed { position, str ->
    //     val end = index + str.length
    //     textSpan.styles[position].forEach {
    //         spanStr.setSpan(it, index, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    //         if (it is URLSpan) this.movementMethod = LinkMovementMethod()
    //     }
    //     index += str.length
    // }
    // text = spanStr
    text = textSpan.toCharSequence()
}

