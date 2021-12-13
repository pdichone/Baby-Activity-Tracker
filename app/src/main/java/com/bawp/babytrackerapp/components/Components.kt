package com.bawp.babytrackerapp.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.bawp.babytrackerapp.navigation.BabyScreens
import com.bawp.babytrackerapp.screens.menu.MenuItems
import com.google.firebase.auth.FirebaseAuth
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Text(text = "Baby Tracker",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.h3,
        color = Color.Red.copy(alpha = 0.5f))
}


@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    isSingleLine: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
              ) {
    InputField(modifier = modifier,
        isSingleLine = isSingleLine,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)


}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
              ) {

    OutlinedTextField(value = valueState.value,
        onValueChange = { valueState.value = it},
        label = { Text(text = labelId)},
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction)


}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
                 ) {

    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = labelId)},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction)

}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible}) {
        Icons.Default.Close

    }

}


@Preview
@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {}) {
    Surface(modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius)),
        color = Color(0xFF92CBDF)) {

        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = TextStyle(color = Color.White,
                fontSize = 15.sp),)

        }

    }


}

@Composable
fun MainAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked:() -> Unit = {}
              ) {

    TopAppBar(title = {
        Row(verticalAlignment = Alignment.CenterVertically){
            if (showProfile) {
                Icon(imageVector = Icons.Default.Favorite,
                    contentDescription = "Logo Icon",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .scale(0.9f)
                    )

            }
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = "arrow back",
                    tint = Color.Red.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { onBackArrowClicked.invoke() })
            }
            Spacer(modifier = Modifier.width(40.dp) )
            Text(text = title,
                color = Color.Red.copy(alpha = 0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))


        }


    },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance()
                    .signOut().run {
                        navController.navigate(BabyScreens.LoginScreen.name)
                    }
            }) {
                if (showProfile) Row() {
                    Icon(imageVector = Icons.Filled.MailOutline ,
                        contentDescription = "Logout" ,
                        // tint = Color.Green.copy(alpha = 0.4f)
                        )
                }else Box {}



            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp)

}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(onClick = { onTap()},
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xFF92CBDF)) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription = "Add a Book",
            tint = Color.White)

    }

}

@Composable
fun TitleSection(modifier: Modifier = Modifier,
                 label: String) {
    Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left)
        }

    }

}

@ExperimentalFoundationApi
@Composable
 fun ShowMenuGrid(data: List<MenuItems>,
                         onItemClicked: (String) -> Unit = {}) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)) {

        items(items = data) { item ->

            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        onItemClicked.invoke(item.title)
                    },
                backgroundColor = Color(0xFFEF9A9A),
                elevation = 4.dp,
                ) {
                Column(
                    modifier = Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                      ) {
                    Image(
                        painter = item.icon, contentDescription = null, modifier = Modifier.size(35.dp)
                         )
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.subtitle2,
                        )
                }
            }

        }
    }
}
@Composable
 fun ButtonChips(items: List<String>,
                 selectedIndex: MutableState<Int>,
                borderValue: Dp = 2.dp,
                borderColor: Color = Color.DarkGray) {
    items.forEachIndexed { index, item ->
        OutlinedButton(
            onClick = { selectedIndex.value = index },
            modifier = when (index) {
                0 -> {
                    if (selectedIndex.value == index) {
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(1f)
                            .clip(shape = CircleShape.copy(all = CornerSize(35.dp)))
                            .border(
                                width = borderValue, color = borderColor, shape = CircleShape
                                   )
                    } else {
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(0f)
                    }
                }
                else -> {
                    val offset = -1 * index
                    if (selectedIndex.value == index) {
                        Modifier
                            .offset(offset.dp, 0.dp)
                            .zIndex(1f)
                            .clip(shape = CircleShape.copy(all = CornerSize(35.dp)))
                            .border(
                                width = borderValue, color = borderColor, shape = CircleShape
                                   )


                    } else {
                        Modifier
                            .offset(offset.dp, 0.dp)
                            .zIndex(0f)
                    }
                }
            },

            )

        {
            Text(
                text = item, color = if (selectedIndex.value == index) {
                    MaterialTheme.colors.primary
                } else {
                    Color.DarkGray.copy(alpha = 0.9f)
                }, modifier = Modifier.padding(horizontal = 8.dp)
                )
        }
    }
}
@Composable
fun ButtonChipsColors(items: List<Color>,
                selectedIndex: MutableState<Int>,
                borderValue: Dp = 2.dp,
                borderColor: Color = Color.DarkGray) {
    items.forEachIndexed { index, item ->
        OutlinedButton(
            onClick = { selectedIndex.value = index },
            modifier = when (index) {
                0 -> {
                    if (selectedIndex.value == index) {
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(1f)
                            .clip(shape = CircleShape.copy(all = CornerSize(35.dp)))
                            .border(
                                width = borderValue,
                                color = borderColor,
                                shape = CircleShape
                                   )
                    } else {
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(0f)
                    }
                }
                else -> {
                    val offset = -1 * index
                    if (selectedIndex.value == index) {
                        Modifier
                            .offset(offset.dp, 0.dp)
                            .zIndex(1f)
                            .clip(shape = CircleShape.copy(all = CornerSize(35.dp)))
                            .border(
                                width = borderValue, color = borderColor, shape = CircleShape
                                   )


                    } else {
                        Modifier
                            .offset(offset.dp, 0.dp)
                            .zIndex(0f)
                    }
                }
            }.wrapContentSize(align = Alignment.Center),
            shape = CircleShape,
           colors = ButtonDefaults.buttonColors(
               backgroundColor = item

                                               )
            )

        {
            Text(
                text = "", color = if (selectedIndex.value == index) {
                    MaterialTheme.colors.primary
                } else {
                    Color.DarkGray.copy(alpha = 0.9f)
                }, modifier = Modifier.padding(horizontal = 8.dp)
                )
        }
    }
}

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG)
        .show()
}


