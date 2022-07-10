package com.bortxapps.thewise.presentation.screens.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.bortxapps.application.pokos.Election
import com.bortxapps.thewise.R
import com.bortxapps.thewise.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

@ExperimentalMaterialApi
@Composable
fun PaintElectionRow(item: Election, navHostController: NavHostController) {

    val colTitle = colorResource(id = R.color.yellow_800)
    val colSubtitle = colorResource(id = R.color.black)
    val colBack = colorResource(id = R.color.transparent)

    var expanded by remember { mutableStateOf(false) }
    var image by remember { mutableStateOf<Bitmap?>(null) }
    var colorTitle by remember { mutableStateOf(colTitle) }
    var colorSubTitle by remember { mutableStateOf(colSubtitle) }
    var colorTextBackground by remember { mutableStateOf(colBack) }

    val scope = rememberCoroutineScope()

    fun openElectionInfo(item: Election) {
        Log.d("Elections", "Click in election card")
        navHostController.navigate(
            Screen.InfoElection.getRouteWithId(item.id.toString())
        )
    }


    fun createPalette(url: String) {

        if (!expanded) {
            colorTextBackground = colBack
            colorSubTitle = colSubtitle
            colorTitle = colTitle
        } else {
            try {
                val url = URL(url)
                scope.launch(Dispatchers.Default) {
                    if (image == null) {
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    }
                    image?.let { img ->
                        Palette.from(img).generate { result ->
                            result?.let { palete ->
                                palete.vibrantSwatch?.let {
                                    colorTextBackground = Color(
                                        Color(it.bodyTextColor).red,
                                        Color(it.bodyTextColor).green,
                                        Color(it.bodyTextColor).blue,
                                        0.3f
                                    )
                                    colorSubTitle = Color(it.rgb)
                                    colorTitle = Color(it.rgb)
                                }
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                System.out.println(e)
            }
        }
    }

    Card(
        onClick = { expanded = !expanded },
        elevation = 2.dp,
        shape = RoundedCornerShape(2.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(0.dp, 150.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        model = image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(colorTextBackground)
            ) {
                Text(
                    text = item.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(bottom = 0.dp, top = 5.dp)
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = colorTitle,
                    maxLines = 1
                )

                item.getWinningOption()?.let {
                    Text(
                        text = "${stringResource(R.string.winning_option_label)} ${it.name}",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 10.dp, top = 0.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        color = colorSubTitle,
                        maxLines = 1
                    )
                } ?: Text(
                    text = stringResource(R.string.no_options_configured),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp, top = 0.dp)
                        .fillMaxWidth(),
                    color = colorResource(id = R.color.black),
                    maxLines = 1
                )
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column {
                item.getWinningOption()?.let { option ->

                    Text(
                        text = "${stringResource(R.string.matching_conditions_label)} ${option.getMatchingConditions()}",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 5.dp)
                            .padding(horizontal = 20.dp),
                        maxLines = 3
                    )
                }

                Row {
                    Button(
                        onClick = { scope.launch { openElectionInfo(item) } },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.edit),
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.subtitle1,
                            color = colorTitle
                        )
                    }
                    Button(
                        onClick = { scope.launch { } },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.delete),
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.subtitle1,
                            color = colorTitle,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}
}