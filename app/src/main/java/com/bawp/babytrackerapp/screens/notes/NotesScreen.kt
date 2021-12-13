package com.bawp.babytrackerapp.screens.notes

import android.app.DatePickerDialog
import android.content.ContentProvider
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import com.bawp.babytrackerapp.components.EmailInput
import com.bawp.babytrackerapp.components.MainAppBar
import com.bawp.babytrackerapp.model.Activity
import com.bawp.babytrackerapp.navigation.BabyScreens
import com.bawp.babytrackerapp.util.saveActivityToFirebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun NotesScreen(navController: NavController) {
    Scaffold(topBar = { MainAppBar(title = "Notes",
        Icons.Default.ArrowBack,
        showProfile = false,
        navController = navController){
        navController.navigate(BabyScreens.MainScreen.name)
    }
    }) {
        NoteForm { note, a, timestamp ->

            if (note.isNotEmpty()) {
                /**
                 * https://stackoverflow.com/questions/5369682/how-to-get-current-time-and-date-in-android
                 * "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
                "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
                "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
                "yyMMddHHmmssZ"-------------------- 010704120856-0700
                "K:mm a, z" ----------------------- 0:08 PM, PDT
                "h:mm a" -------------------------- 12:08 PM
                "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
                 */
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a")
                val myDate: String =  current.format(formatter)
                Log.d("Date", "DATE: ${myDate.split(" ")}")
                val mNote = Activity(
                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    babyId = "IdHoPLrao8NpyWKS0d1G", //for now
                    date = myDate.split(" ")[0],
                    timeEntered = myDate.split(" ")[1]+" "+myDate.split(" ")[2] ,
                    note = note,
                    activityType = "Notes"
                                    )


                //saveActivityToFirebase(mNote, navController)
            }

        }

    }

}
//Todo: https://medium.com/geekculture/a-simple-way-to-work-with-excel-in-android-app-94c727e9a138

//private fun createWorkbook(): Workbook {
//    // Creating excel workbook
//    val workbook = XSSFWorkbook()
//
//    //Creating first sheet inside workbook
//    //Constants.SHEET_NAME is a string value of sheet name
//    val sheet: Sheet = workbook.createSheet("baby_activities")
//
//    //Create Header Cell Style
//    val cellStyle = getHeaderStyle(workbook)
//
//    //Creating sheet header row
//    createSheetHeader(cellStyle, sheet)
//
//    //Adding data to the sheet
//    addData(0, sheet)
//
//    return workbook
//}
//
//private fun createSheetHeader(cellStyle: CellStyle, sheet: Sheet) {
//    //setHeaderStyle is a custom function written below to add header style
//
//    //Create sheet first row
//    val row = sheet.createRow(0)
//
//    //Header list
//    val HEADER_LIST = listOf("column_1", "column_2", "column_3")
//
//    //Loop to populate each column of header row
//    for ((index, value) in HEADER_LIST.withIndex()) {
//
//        val columnWidth = (15 * 500)
//
//        //index represents the column number
//        sheet.setColumnWidth(index, columnWidth)
//
//        //Create cell
//        val cell = row.createCell(index)
//
//        //value represents the header value from HEADER_LIST
//        cell?.setCellValue(value)
//
//        //Apply style to cell
//        cell.cellStyle = cellStyle
//    }
//}
//
//private fun getHeaderStyle(workbook: Workbook): CellStyle {
//
//    //Cell style for header row
//    val cellStyle: CellStyle = workbook.createCellStyle()
//
//    //Apply cell color
//    val colorMap: IndexedColorMap = (workbook as XSSFWorkbook).stylesSource.indexedColors
//    var color = XSSFColor(IndexedColors.RED, colorMap).indexed
//    cellStyle.fillForegroundColor = color
//    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
//
//    //Apply font style on cell text
//    val whiteFont = workbook.createFont()
//    color = XSSFColor(IndexedColors.WHITE, colorMap).indexed
//    whiteFont.color = color
//    whiteFont.bold = true
//    cellStyle.setFont(whiteFont)
//
//
//    return cellStyle
//}
//
//private fun addData(rowIndex: Int, sheet: Sheet) {
//
//    //Create row based on row index
//    val row = sheet.createRow(rowIndex)
//
//    //Add data to each cell
//    createCell(row, 0, "value 1") //Column 1
//    createCell(row, 1, "value 2") //Column 2
//    createCell(row, 2, "value 3") //Column 3
//}
//
//private fun createCell(row: Row, columnIndex: Int, value: String?) {
//    val cell = row.createCell(columnIndex)
//    cell?.setCellValue(value)
//}
//
//private fun createExcel(workbook: Workbook) {
//
//    //Get App Director, APP_DIRECTORY_NAME is a string
//    val appDirectory = requireContext().getExternalFilesDir("baby_activity_dir")
//
//    //Check App Directory whether it exists or not, create if not.
//    if (appDirectory != null && !appDirectory.exists()) {
//        appDirectory.mkdirs()
//    }
//
//    //Create excel file with extension .xlsx
//    val excelFile = File(appDirectory,"baby_activity_file")
//
//    //Write workbook to file using FileOutputStream
//    try {
//        val fileOut = FileOutputStream(excelFile)
//        workbook.write(fileOut)
//        fileOut.close()
//    } catch (e: FileNotFoundException) {
//        e.printStackTrace()
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//}

@ExperimentalComposeUiApi
@Composable
fun NoteForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String, Timestamp) -> Unit = { name, dob, timeStamp ->}
            ) {

    val year: Int
    val month: Int
    val day: Int
    val mHour: Int
    val mMinute: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    // Get Current Time

    mHour = calendar.get(Calendar.HOUR_OF_DAY);
    mMinute = calendar.get(Calendar.MINUTE);

    val context = LocalContext.current

    val name = rememberSaveable { mutableStateOf("") }
    val date = remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(name.value, date.value) {
        name.value.trim().isNotEmpty() && date.value.trim().isNotEmpty()

    }

    val dateState = remember {
        mutableStateOf(Timestamp(calendar.time))
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "${month+1}/$dayOfMonth/$year"

            calendar.set(year , month, dayOfMonth)
            calendar.time
            //Date(year, month, dayOfMonth)
            dateState.value = Timestamp(calendar.time)

        }, year, month, day)

    val modifier = Modifier
        .height(300.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())

    Column(modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        EmailInput(
            modifier = Modifier.height(220.dp),
            emailState = name,
            labelId = "Enter a note...",
            isSingleLine = false,
            enabled = !loading,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(name.value, date.value, dateState.value)
            },)

        Button(onClick = {
            onDone(name.value, date.value, dateState.value)
            keyboardController?.hide()
        }) {
            Text(text = "Save")
        }
    }
}

