package com.flamingo.playground.view.theme

/**
 * Demo of the theme attribute
 * @param attrName e.g.: android:colorBackground
 * @param explanation long explanation of the purpose and usage details of the attr, in html
 */
data class AttrDoc(
    val type: AttrType,
    val attrName: String,
    val explanation: String,
    val explanationInHtml: Boolean = false,
    val deprecated: Boolean = false,
) {
    companion object {
        /** 📖 */
        private const val ATTR_COMMENT_PREFIX = " \uD83D\uDCD6"

        fun isDocComment(comment: String) = comment.startsWith(ATTR_COMMENT_PREFIX)

        /** @return null, if parcing has failed */
        @Suppress("MagicNumber")
        fun fromDocComment(docComment: String): AttrDoc? = runCatching {
            val drop = docComment.drop(6)
            var explanation = drop.dropWhile { it != ',' }.drop(1).trimIndent()
            var explanationInHtml = false
            if (explanation.startsWith("html")) {
                explanation = explanation.drop(5)
                explanationInHtml = true
            }
            return AttrDoc(
                type = AttrType.fromEmoji(docComment.substring(3, 5)),
                attrName = drop.takeWhile { it != ',' }.trim(),
                explanation = explanation,
                explanationInHtml = explanationInHtml,
                deprecated = docComment.substringBefore('\n').contains('⚰'),
            )
        }.getOrNull()
    }
}
