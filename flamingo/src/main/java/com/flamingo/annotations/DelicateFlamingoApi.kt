package com.flamingo.annotations

@RequiresOptIn(
    """This is a delicate API and its use requires care! In many cases, there are restrictions 
in terms of defined behaviour and backwards compatibility of this API. Make sure you fully read and 
understood documentation of the declaration that is marked as a delicate API""",
    level = RequiresOptIn.Level.ERROR
)
@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
public annotation class DelicateFlamingoApi
