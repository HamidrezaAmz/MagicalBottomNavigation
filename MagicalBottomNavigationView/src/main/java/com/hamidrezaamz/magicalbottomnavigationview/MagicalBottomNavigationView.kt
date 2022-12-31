package com.hamidrezaamz.magicalbottomnavigationview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.view.get
import androidx.core.view.size
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hamidrezaamz.magicalbottomnavigationview.common.PublicValues.Companion.patch
import com.hamidrezaamz.magicalbottomnavigationview.common.PublicValues.Companion.radius
import com.hamidrezaamz.magicalbottomnavigationview.common.PublicValues.Companion.showControllerPoints

private const val TAG = "MagicalBottomNavigation"

class MagicalBottomNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    private val mPath: Path
    private val mPaint: Paint

    // the coordinates of the first curve
    private val mFirstCurveStartPoint = Point()
    private val mFirstCurveEndPoint = Point()
    private val mFirstCurveControlPoint1 = Point()
    private val mFirstCurveControlPoint2 = Point()

    //the coordinates of the second curve
    private var mSecondCurveStartPoint = Point()
    private val mSecondCurveEndPoint = Point()
    private val mSecondCurveControlPoint1 = Point()
    private val mSecondCurveControlPoint2 = Point()

    // Navigation bar bounds (width & height)
    private var mNavigationBarWidth: Int = 0
    private var mNavigationBarHeight: Int = 0

    // controller points
    var x1 = -1
    var x2 = -1
    var x6 = -1
    var y1 = -1
    var y2 = -1

    init {
        // radius of fab button
        radius = 256 / 2
        mPath = Path()
        mPaint = Paint()
        with(mPaint) {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.WHITE
        }
        setBackgroundColor(Color.TRANSPARENT)
        itemIconTintList = null
    }

    override fun setOnItemReselectedListener(listener: OnItemReselectedListener?) {
        super.setOnItemReselectedListener(listener)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        initPoints()
        drawShape()

        mPaint.color = Color.WHITE
        canvas?.drawPath(mPath, mPaint)

        if (showControllerPoints) {
            mPaint.color = Color.RED
            canvas?.drawCircle(
                mFirstCurveControlPoint1.x.toFloat(), mFirstCurveControlPoint1.y.toFloat(), (5).toFloat(), mPaint
            )

            mPaint.color = Color.GREEN
            canvas?.drawCircle(
                mFirstCurveControlPoint2.x.toFloat(), mFirstCurveControlPoint2.y.toFloat(), (5).toFloat(), mPaint
            )

            mPaint.color = Color.BLUE
            canvas?.drawCircle(
                mSecondCurveControlPoint1.x.toFloat(), mSecondCurveControlPoint1.y.toFloat(), (5).toFloat(), mPaint
            )

            mPaint.color = Color.MAGENTA
            canvas?.drawCircle(
                mSecondCurveControlPoint2.x.toFloat(), mSecondCurveControlPoint2.y.toFloat(), (5).toFloat(), mPaint
            )
        }

        // canvas?.rotate(2.0f, -1.0f, 0.0f)

    }

    private fun initPoints() {
        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        mNavigationBarWidth = width
        mNavigationBarHeight = height
        // controller points
        x1 = 0
        x2 = mNavigationBarWidth / 5
        x6 = mNavigationBarWidth
        y1 = 0
        y2 = mNavigationBarHeight / 2
    }

    private fun templatePointsGenerator() {
        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(mNavigationBarWidth / 2 - radius * 2 - radius / 3, 0)
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(mNavigationBarWidth / 2, radius + radius / 4)
        // same thing for the second curve
        mSecondCurveStartPoint = mFirstCurveEndPoint
        mSecondCurveEndPoint.set(mNavigationBarWidth / 2 + radius * 2 + radius / 3, 0)

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        mFirstCurveControlPoint1.set(
            mFirstCurveStartPoint.x + radius + radius / 4, mFirstCurveStartPoint.y
        )
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        mFirstCurveControlPoint2.set(
            mFirstCurveEndPoint.x - radius * 2 + radius, mFirstCurveEndPoint.y
        )

        mSecondCurveControlPoint1.set(
            mSecondCurveStartPoint.x + radius * 2 - radius, mSecondCurveStartPoint.y
        )
        mSecondCurveControlPoint2.set(
            mSecondCurveEndPoint.x - (radius + radius / 4), mSecondCurveEndPoint.y
        )

        mPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat())

            cubicTo(
                mFirstCurveControlPoint1.x.toFloat(),
                mFirstCurveControlPoint1.y.toFloat(),
                mFirstCurveControlPoint2.x.toFloat(),
                mFirstCurveControlPoint2.y.toFloat(),
                mFirstCurveEndPoint.x.toFloat(),
                mFirstCurveEndPoint.y.toFloat()
            )

            cubicTo(
                mSecondCurveControlPoint1.x.toFloat(),
                mSecondCurveControlPoint1.y.toFloat(),
                mSecondCurveControlPoint2.x.toFloat(),
                mSecondCurveControlPoint2.y.toFloat(),
                mSecondCurveEndPoint.x.toFloat(),
                mSecondCurveEndPoint.y.toFloat()
            )

            lineTo(mNavigationBarWidth.toFloat(), 0f)
            lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
            lineTo(0f, mNavigationBarHeight.toFloat())
            close()
        }
    }

    private fun isRTL(): Boolean {
        return resources.getBoolean(R.bool.is_right_to_left)
    }

    private fun drawShape() {
        if (isRTL()) {
            when (getSelectionItemPosition()) {
                0 -> drawShape4th()
                1 -> drawShape3th()
                2 -> drawShape2th()
                3 -> drawShape1th()
                4 -> drawShape0th()
            }
        } else {
            when (getSelectionItemPosition()) {
                0 -> drawShape0th()
                1 -> drawShape1th()
                2 -> drawShape2th()
                3 -> drawShape3th()
                4 -> drawShape4th()
            }
        }
    }

    private fun drawShape0th() {
        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(x1, y1)
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(x2 / 2, y2)

        // same thing for the second curve
        mSecondCurveStartPoint.set(x2 / 2, y2)
        mSecondCurveEndPoint.set(x6, y1)

        // First Curve Control Point: RED
        mFirstCurveControlPoint1.set(
            mFirstCurveStartPoint.x + x2 / 4, mFirstCurveStartPoint.y + y2
        )
        // First Curve Control Point: GREEN
        mFirstCurveControlPoint2.set(
            mFirstCurveEndPoint.x, mFirstCurveEndPoint.y
        )

        // Second Curve Control Point: BLUE
        mSecondCurveControlPoint1.set(
            mSecondCurveStartPoint.x + x2, mSecondCurveStartPoint.y - y2 / 3
        )
        // Second Curve Control Point: MAGENTA
        mSecondCurveControlPoint2.set(
            mSecondCurveEndPoint.x - patch, mSecondCurveEndPoint.y
        )

        mPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat())

            cubicTo(
                mFirstCurveControlPoint1.x.toFloat(),
                mFirstCurveControlPoint1.y.toFloat(),
                mFirstCurveControlPoint2.x.toFloat(),
                mFirstCurveControlPoint2.y.toFloat(),
                mFirstCurveEndPoint.x.toFloat(),
                mFirstCurveEndPoint.y.toFloat()
            )

            cubicTo(
                mSecondCurveControlPoint1.x.toFloat(),
                mSecondCurveControlPoint1.y.toFloat(),
                mSecondCurveControlPoint2.x.toFloat(),
                mSecondCurveControlPoint2.y.toFloat(),
                mSecondCurveEndPoint.x.toFloat(),
                mSecondCurveEndPoint.y.toFloat()
            )

            lineTo(mNavigationBarWidth.toFloat(), 0f)
            lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
            lineTo(0f, mNavigationBarHeight.toFloat())

            close()
        }
    }

    private fun drawShape1th() {
        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(x1, y1)
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(x2 + x2 / 2, y2)

        // same thing for the second curve
        mSecondCurveStartPoint.set(x2 + x2 / 2, y2)
        mSecondCurveEndPoint.set(x6, y1)

        // First Curve Control Point: RED
        mFirstCurveControlPoint1.set(
            mFirstCurveStartPoint.x + x2, mFirstCurveStartPoint.y + mNavigationBarHeight - mNavigationBarHeight / 2
        )
        // First Curve Control Point: GREEN
        mFirstCurveControlPoint2.set(
            mFirstCurveEndPoint.x - mFirstCurveEndPoint.x / 18, mFirstCurveEndPoint.y /*- mNavigationBarHeight / 12*/
        )

        // Second Curve Control Point: BLUE
        mSecondCurveControlPoint1.set(
            mSecondCurveStartPoint.x + radius * 2 - radius, mSecondCurveStartPoint.y
        )
        // Second Curve Control Point: MAGENTA
        mSecondCurveControlPoint2.set(
            mSecondCurveEndPoint.x - (radius + radius / 4), mSecondCurveEndPoint.y
        )

        mPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat())

            cubicTo(
                mFirstCurveControlPoint1.x.toFloat(),
                mFirstCurveControlPoint1.y.toFloat(),
                mFirstCurveControlPoint2.x.toFloat(),
                mFirstCurveControlPoint2.y.toFloat(),
                mFirstCurveEndPoint.x.toFloat(),
                mFirstCurveEndPoint.y.toFloat()
            )

            cubicTo(
                mSecondCurveControlPoint1.x.toFloat(),
                mSecondCurveControlPoint1.y.toFloat(),
                mSecondCurveControlPoint2.x.toFloat(),
                mSecondCurveControlPoint2.y.toFloat(),
                mSecondCurveEndPoint.x.toFloat(),
                mSecondCurveEndPoint.y.toFloat()
            )

            lineTo(mNavigationBarWidth.toFloat(), 0f)
            lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
            lineTo(0f, mNavigationBarHeight.toFloat())

            close()
        }
    }

    private fun drawShape2th() {
        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        mNavigationBarWidth = width
        mNavigationBarHeight = height

        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(x1, y1)
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(2 * x2, y2)

        // same thing for the second curve
        mSecondCurveStartPoint.set(2 * x2, y2)
        mSecondCurveEndPoint.set(x6, y1)

        // First Curve Control Point: RED
        mFirstCurveControlPoint1.set(
            mFirstCurveStartPoint.x + 2 * x2, mFirstCurveStartPoint.y + y2
        )
        // First Curve Control Point: GREEN
        mFirstCurveControlPoint2.set(
            mFirstCurveEndPoint.x + 2 * x2, mFirstCurveStartPoint.y + y2
        )
        // Second Curve Control Point: BLUE
        mSecondCurveControlPoint1.set(
            mSecondCurveStartPoint.x + radius * 2 - radius, mSecondCurveStartPoint.y
        )
        // Second Curve Control Point: MAGENTA
        mSecondCurveControlPoint2.set(
            mSecondCurveEndPoint.x - (radius + radius / 4), mSecondCurveEndPoint.y
        )

        mPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat())

            cubicTo(
                mFirstCurveControlPoint1.x.toFloat(),
                mFirstCurveControlPoint1.y.toFloat(),
                mFirstCurveControlPoint2.x.toFloat(),
                mFirstCurveControlPoint2.y.toFloat(),
                mFirstCurveEndPoint.x.toFloat(),
                mFirstCurveEndPoint.y.toFloat()
            )

            cubicTo(
                mSecondCurveControlPoint1.x.toFloat(),
                mSecondCurveControlPoint1.y.toFloat(),
                mSecondCurveControlPoint2.x.toFloat(),
                mSecondCurveControlPoint2.y.toFloat(),
                mSecondCurveEndPoint.x.toFloat(),
                mSecondCurveEndPoint.y.toFloat()
            )

            lineTo(mNavigationBarWidth.toFloat(), 0f)
            lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
            lineTo(0f, mNavigationBarHeight.toFloat())

            close()
        }
    }

    private fun drawShape3th() {
        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        mNavigationBarWidth = width
        mNavigationBarHeight = height

        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(x6, y1)
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(x6 - x2 / 2, y2)

        // same thing for the second curve
        mSecondCurveStartPoint.set(x6 - x2 / 2, y2)
        mSecondCurveEndPoint.set(x1, y1)

        // First Curve Control Point: RED
        mFirstCurveControlPoint1.set(
            mFirstCurveStartPoint.x + mNavigationBarWidth - mNavigationBarWidth / 3,
            mFirstCurveStartPoint.y + mNavigationBarHeight - mNavigationBarHeight / 2
        )
        // First Curve Control Point: GREEN
        mFirstCurveControlPoint2.set(
            mFirstCurveEndPoint.x + mNavigationBarWidth / 8, mFirstCurveEndPoint.y - mNavigationBarHeight / 10
        )
        // Second Curve Control Point: BLUE
        mSecondCurveControlPoint1.set(
            mSecondCurveStartPoint.x + radius * 2 - radius, mSecondCurveStartPoint.y
        )
        // Second Curve Control Point: MAGENTA
        mSecondCurveControlPoint2.set(
            mSecondCurveEndPoint.x - (radius + radius / 4), mSecondCurveEndPoint.y
        )

        mPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat())

            cubicTo(
                mFirstCurveControlPoint1.x.toFloat(),
                mFirstCurveControlPoint1.y.toFloat(),
                mFirstCurveControlPoint2.x.toFloat(),
                mFirstCurveControlPoint2.y.toFloat(),
                mFirstCurveEndPoint.x.toFloat(),
                mFirstCurveEndPoint.y.toFloat()
            )

            cubicTo(
                mSecondCurveControlPoint1.x.toFloat(),
                mSecondCurveControlPoint1.y.toFloat(),
                mSecondCurveControlPoint2.x.toFloat(),
                mSecondCurveControlPoint2.y.toFloat(),
                mSecondCurveEndPoint.x.toFloat(),
                mSecondCurveEndPoint.y.toFloat()
            )

            lineTo(mNavigationBarWidth.toFloat(), 0f)
            lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
            lineTo(0f, mNavigationBarHeight.toFloat())

            close()
        }
    }

    private fun drawShape4th() {
        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(x1, y1)
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(x2 / 2, y2)

        // same thing for the second curve
        mSecondCurveStartPoint.set(x2 / 2, y2)
        mSecondCurveEndPoint.set(x6, y1)

        // First Curve Control Point: RED
        mFirstCurveControlPoint1.set(
            mFirstCurveStartPoint.x + x2 / 4, mFirstCurveStartPoint.y + y2
        )
        // First Curve Control Point: GREEN
        mFirstCurveControlPoint2.set(
            mFirstCurveEndPoint.x, mFirstCurveEndPoint.y
        )

        // Second Curve Control Point: BLUE
        mSecondCurveControlPoint1.set(
            mSecondCurveStartPoint.x + x2, mSecondCurveStartPoint.y - y2 / 3
        )
        // Second Curve Control Point: MAGENTA
        mSecondCurveControlPoint2.set(
            mSecondCurveEndPoint.x - patch, mSecondCurveEndPoint.y
        )

        mPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(mFirstCurveStartPoint.x.toFloat(), mFirstCurveStartPoint.y.toFloat())

            cubicTo(
                mFirstCurveControlPoint1.x.toFloat(),
                mFirstCurveControlPoint1.y.toFloat(),
                mFirstCurveControlPoint2.x.toFloat(),
                mFirstCurveControlPoint2.y.toFloat(),
                mFirstCurveEndPoint.x.toFloat(),
                mFirstCurveEndPoint.y.toFloat()
            )

            cubicTo(
                mSecondCurveControlPoint1.x.toFloat(),
                mSecondCurveControlPoint1.y.toFloat(),
                mSecondCurveControlPoint2.x.toFloat(),
                mSecondCurveControlPoint2.y.toFloat(),
                mSecondCurveEndPoint.x.toFloat(),
                mSecondCurveEndPoint.y.toFloat()
            )

            lineTo(mNavigationBarWidth.toFloat(), 0f)
            lineTo(mNavigationBarWidth.toFloat(), mNavigationBarHeight.toFloat())
            lineTo(0f, mNavigationBarHeight.toFloat())

            close()
        }
    }

    private fun getSelectionItemPosition(): Int {
        val menu = menu
        for (i in 0 until menu.size) {
            if (menu[i].itemId == selectedItemId) {
                return i
            }
        }
        return 0
    }
}