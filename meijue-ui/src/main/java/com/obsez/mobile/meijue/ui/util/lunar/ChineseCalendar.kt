package com.obsez.mobile.meijue.ui.util.lunar

import java.util.*

/**
 * ChineseCalendar.java
 * author: hedzr
 * 中国农历算法 - 实用于公历 1901 年至 2100 年之间的 200 年
 */

class ChineseCalendar {
    
    companion object Factory {
        /**
         * Usage:
         *
         * val cc: ChineseCalendar = ChineseCalendar.newInstance(2018, 3, 7)
         * println cc
         *
         */
        fun newInstance(cal: Calendar = Calendar.getInstance()): ChineseCalendar {
            val c = ChineseCalendar()
            c.setGregorian(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE))
            c.computeChineseFields()
            c.computeSolarTerms()
            return c
        }
        
        fun newInstance(y: Int, m: Int, d: Int): ChineseCalendar {
            val c = ChineseCalendar()
            c.setGregorian(y, m, d)
            c.computeChineseFields()
            c.computeSolarTerms()
            return c
        }
        
        
        val daysInGregorianMonth = charArrayOf(31.toChar(), 28.toChar(), 31.toChar(), 30.toChar(), 31.toChar(), 30.toChar(), 31.toChar(), 31.toChar(), 30.toChar(), 31.toChar(), 30.toChar(), 31.toChar())
        val stemNames = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
        val branchNames = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
        val animalNames = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")
        
        private val chineseMonths = charArrayOf(
                // 农历月份大小压缩表，两个字节表示一年。两个字节共十六个二进制位数，
                // 前四个位数表示闰月月份，后十二个位数表示十二个农历月份的大小。
                0x00.toChar(), 0x04.toChar(), 0xad.toChar(), 0x08.toChar(), 0x5a.toChar(), 0x01.toChar(), 0xd5.toChar(), 0x54.toChar(), 0xb4.toChar(), 0x09.toChar(), 0x64.toChar(), 0x05.toChar(), 0x59.toChar(), 0x45.toChar(), 0x95.toChar(), 0x0a.toChar(), 0xa6.toChar(), 0x04.toChar(), 0x55.toChar(), 0x24.toChar(), 0xad.toChar(), 0x08.toChar(), 0x5a.toChar(), 0x62.toChar(), 0xda.toChar(), 0x04.toChar(), 0xb4.toChar(), 0x05.toChar(), 0xb4.toChar(), 0x55.toChar(), 0x52.toChar(), 0x0d.toChar(), 0x94.toChar(), 0x0a.toChar(), 0x4a.toChar(), 0x2a.toChar(), 0x56.toChar(), 0x02.toChar(), 0x6d.toChar(), 0x71.toChar(), 0x6d.toChar(), 0x01.toChar(), 0xda.toChar(), 0x02.toChar(), 0xd2.toChar(), 0x52.toChar(), 0xa9.toChar(), 0x05.toChar(), 0x49.toChar(), 0x0d.toChar(), 0x2a.toChar(), 0x45.toChar(), 0x2b.toChar(), 0x09.toChar(), 0x56.toChar(), 0x01.toChar(), 0xb5.toChar(), 0x20.toChar(), 0x6d.toChar(), 0x01.toChar(), 0x59.toChar(), 0x69.toChar(), 0xd4.toChar(), 0x0a.toChar(), 0xa8.toChar(), 0x05.toChar(), 0xa9.toChar(), 0x56.toChar(), 0xa5.toChar(), 0x04.toChar(), 0x2b.toChar(), 0x09.toChar(), 0x9e.toChar(), 0x38.toChar(), 0xb6.toChar(), 0x08.toChar(), 0xec.toChar(), 0x74.toChar(), 0x6c.toChar(), 0x05.toChar(), 0xd4.toChar(), 0x0a.toChar(), 0xe4.toChar(), 0x6a.toChar(), 0x52.toChar(), 0x05.toChar(), 0x95.toChar(), 0x0a.toChar(), 0x5a.toChar(), 0x42.toChar(), 0x5b.toChar(), 0x04.toChar(), 0xb6.toChar(), 0x04.toChar(), 0xb4.toChar(), 0x22.toChar(), 0x6a.toChar(), 0x05.toChar(), 0x52.toChar(), 0x75.toChar(), 0xc9.toChar(), 0x0a.toChar(), 0x52.toChar(), 0x05.toChar(), 0x35.toChar(), 0x55.toChar(), 0x4d.toChar(), 0x0a.toChar(), 0x5a.toChar(), 0x02.toChar(), 0x5d.toChar(), 0x31.toChar(), 0xb5.toChar(), 0x02.toChar(), 0x6a.toChar(), 0x8a.toChar(), 0x68.toChar(), 0x05.toChar(), 0xa9.toChar(), 0x0a.toChar(), 0x8a.toChar(), 0x6a.toChar(), 0x2a.toChar(), 0x05.toChar(), 0x2d.toChar(), 0x09.toChar(), 0xaa.toChar(), 0x48.toChar(), 0x5a.toChar(), 0x01.toChar(), 0xb5.toChar(), 0x09.toChar(), 0xb0.toChar(), 0x39.toChar(), 0x64.toChar(), 0x05.toChar(), 0x25.toChar(), 0x75.toChar(), 0x95.toChar(), 0x0a.toChar(), 0x96.toChar(), 0x04.toChar(), 0x4d.toChar(), 0x54.toChar(), 0xad.toChar(), 0x04.toChar(), 0xda.toChar(), 0x04.toChar(), 0xd4.toChar(), 0x44.toChar(), 0xb4.toChar(), 0x05.toChar(), 0x54.toChar(), 0x85.toChar(), 0x52.toChar(), 0x0d.toChar(), 0x92.toChar(), 0x0a.toChar(), 0x56.toChar(), 0x6a.toChar(), 0x56.toChar(), 0x02.toChar(), 0x6d.toChar(), 0x02.toChar(), 0x6a.toChar(), 0x41.toChar(), 0xda.toChar(), 0x02.toChar(), 0xb2.toChar(), 0xa1.toChar(), 0xa9.toChar(), 0x05.toChar(), 0x49.toChar(), 0x0d.toChar(), 0x0a.toChar(), 0x6d.toChar(), 0x2a.toChar(), 0x09.toChar(), 0x56.toChar(), 0x01.toChar(), 0xad.toChar(), 0x50.toChar(), 0x6d.toChar(), 0x01.toChar(), 0xd9.toChar(), 0x02.toChar(), 0xd1.toChar(), 0x3a.toChar(), 0xa8.toChar(), 0x05.toChar(), 0x29.toChar(), 0x85.toChar(), 0xa5.toChar(), 0x0c.toChar(), 0x2a.toChar(), 0x09.toChar(), 0x96.toChar(), 0x54.toChar(), 0xb6.toChar(), 0x08.toChar(), 0x6c.toChar(), 0x09.toChar(), 0x64.toChar(), 0x45.toChar(), 0xd4.toChar(), 0x0a.toChar(), 0xa4.toChar(), 0x05.toChar(), 0x51.toChar(), 0x25.toChar(), 0x95.toChar(), 0x0a.toChar(), 0x2a.toChar(), 0x72.toChar(), 0x5b.toChar(), 0x04.toChar(), 0xb6.toChar(), 0x04.toChar(), 0xac.toChar(), 0x52.toChar(), 0x6a.toChar(), 0x05.toChar(), 0xd2.toChar(), 0x0a.toChar(), 0xa2.toChar(), 0x4a.toChar(), 0x4a.toChar(), 0x05.toChar(), 0x55.toChar(), 0x94.toChar(), 0x2d.toChar(), 0x0a.toChar(), 0x5a.toChar(), 0x02.toChar(), 0x75.toChar(), 0x61.toChar(), 0xb5.toChar(), 0x02.toChar(), 0x6a.toChar(), 0x03.toChar(), 0x61.toChar(), 0x45.toChar(), 0xa9.toChar(), 0x0a.toChar(), 0x4a.toChar(), 0x05.toChar(), 0x25.toChar(), 0x25.toChar(), 0x2d.toChar(), 0x09.toChar(), 0x9a.toChar(), 0x68.toChar(), 0xda.toChar(), 0x08.toChar(), 0xb4.toChar(), 0x09.toChar(), 0xa8.toChar(), 0x59.toChar(), 0x54.toChar(), 0x03.toChar(), 0xa5.toChar(), 0x0a.toChar(), 0x91.toChar(), 0x3a.toChar(), 0x96.toChar(), 0x04.toChar(), 0xad.toChar(), 0xb0.toChar(), 0xad.toChar(), 0x04.toChar(), 0xda.toChar(), 0x04.toChar(), 0xf4.toChar(), 0x62.toChar(), 0xb4.toChar(), 0x05.toChar(), 0x54.toChar(), 0x0b.toChar(), 0x44.toChar(), 0x5d.toChar(), 0x52.toChar(), 0x0a.toChar(), 0x95.toChar(), 0x04.toChar(), 0x55.toChar(), 0x22.toChar(), 0x6d.toChar(), 0x02.toChar(), 0x5a.toChar(), 0x71.toChar(), 0xda.toChar(), 0x02.toChar(), 0xaa.toChar(), 0x05.toChar(), 0xb2.toChar(), 0x55.toChar(), 0x49.toChar(), 0x0b.toChar(), 0x4a.toChar(), 0x0a.toChar(), 0x2d.toChar(), 0x39.toChar(), 0x36.toChar(), 0x01.toChar(), 0x6d.toChar(), 0x80.toChar(), 0x6d.toChar(), 0x01.toChar(), 0xd9.toChar(), 0x02.toChar(), 0xe9.toChar(), 0x6a.toChar(), 0xa8.toChar(), 0x05.toChar(), 0x29.toChar(), 0x0b.toChar(), 0x9a.toChar(), 0x4c.toChar(), 0xaa.toChar(), 0x08.toChar(), 0xb6.toChar(), 0x08.toChar(), 0xb4.toChar(), 0x38.toChar(), 0x6c.toChar(), 0x09.toChar(), 0x54.toChar(), 0x75.toChar(), 0xd4.toChar(), 0x0a.toChar(), 0xa4.toChar(), 0x05.toChar(), 0x45.toChar(), 0x55.toChar(), 0x95.toChar(), 0x0a.toChar(), 0x9a.toChar(), 0x04.toChar(), 0x55.toChar(), 0x44.toChar(), 0xb5.toChar(), 0x04.toChar(), 0x6a.toChar(), 0x82.toChar(), 0x6a.toChar(), 0x05.toChar(), 0xd2.toChar(), 0x0a.toChar(), 0x92.toChar(), 0x6a.toChar(), 0x4a.toChar(), 0x05.toChar(), 0x55.toChar(), 0x0a.toChar(), 0x2a.toChar(), 0x4a.toChar(), 0x5a.toChar(), 0x02.toChar(), 0xb5.toChar(), 0x02.toChar(), 0xb2.toChar(), 0x31.toChar(), 0x69.toChar(), 0x03.toChar(), 0x31.toChar(), 0x73.toChar(), 0xa9.toChar(), 0x0a.toChar(), 0x4a.toChar(), 0x05.toChar(), 0x2d.toChar(), 0x55.toChar(), 0x2d.toChar(), 0x09.toChar(), 0x5a.toChar(), 0x01.toChar(), 0xd5.toChar(), 0x48.toChar(), 0xb4.toChar(), 0x09.toChar(), 0x68.toChar(), 0x89.toChar(), 0x54.toChar(), 0x0b.toChar(), 0xa4.toChar(), 0x0a.toChar(), 0xa5.toChar(), 0x6a.toChar(), 0x95.toChar(), 0x04.toChar(), 0xad.toChar(), 0x08.toChar(), 0x6a.toChar(), 0x44.toChar(), 0xda.toChar(), 0x04.toChar(), 0x74.toChar(), 0x05.toChar(), 0xb0.toChar(), 0x25.toChar(), 0x54.toChar(), 0x03.toChar())
        
        val bigLeapMonthYears = intArrayOf(
                // 大闰月的闰年年份
                6, 14, 19, 25, 33, 36, 38, 41, 44, 52, 55, 79, 117, 136, 147, 150, 155, 158, 185, 193)
        
        private val sectionalTermMap = arrayOf(charArrayOf(7.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 4.toChar(), 5.toChar(), 5.toChar()), charArrayOf(5.toChar(), 4.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 4.toChar(), 4.toChar(), 5.toChar(), 5.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 3.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 3.toChar(), 3.toChar(), 4.toChar(), 4.toChar(), 3.toChar(), 3.toChar(), 3.toChar()), charArrayOf(6.toChar(), 6.toChar(), 6.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 4.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar()), charArrayOf(5.toChar(), 5.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 4.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 4.toChar(), 4.toChar(), 5.toChar(), 5.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 5.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 4.toChar(), 5.toChar()), charArrayOf(6.toChar(), 6.toChar(), 6.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 4.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar()), charArrayOf(6.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 6.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 6.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 4.toChar(), 5.toChar(), 5.toChar(), 5.toChar(), 5.toChar()), charArrayOf(7.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 7.toChar(), 7.toChar()), charArrayOf(8.toChar(), 8.toChar(), 8.toChar(), 9.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 7.toChar()), charArrayOf(8.toChar(), 8.toChar(), 8.toChar(), 9.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 7.toChar()), charArrayOf(9.toChar(), 9.toChar(), 9.toChar(), 9.toChar(), 8.toChar(), 9.toChar(), 9.toChar(), 9.toChar(), 8.toChar(), 8.toChar(), 9.toChar(), 9.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 9.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 8.toChar()), charArrayOf(8.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 7.toChar()), charArrayOf(7.toChar(), 8.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 8.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 7.toChar(), 7.toChar(), 6.toChar(), 6.toChar(), 6.toChar(), 7.toChar(), 7.toChar()))
        private val sectionalTermYear = arrayOf(charArrayOf(13.toChar(), 49.toChar(), 85.toChar(), 117.toChar(), 149.toChar(), 185.toChar(), 201.toChar(), 250.toChar(), 250.toChar()), charArrayOf(13.toChar(), 45.toChar(), 81.toChar(), 117.toChar(), 149.toChar(), 185.toChar(), 201.toChar(), 250.toChar(), 250.toChar()), charArrayOf(13.toChar(), 48.toChar(), 84.toChar(), 112.toChar(), 148.toChar(), 184.toChar(), 200.toChar(), 201.toChar(), 250.toChar()), charArrayOf(13.toChar(), 45.toChar(), 76.toChar(), 108.toChar(), 140.toChar(), 172.toChar(), 200.toChar(), 201.toChar(), 250.toChar()), charArrayOf(13.toChar(), 44.toChar(), 72.toChar(), 104.toChar(), 132.toChar(), 168.toChar(), 200.toChar(), 201.toChar(), 250.toChar()), charArrayOf(5.toChar(), 33.toChar(), 68.toChar(), 96.toChar(), 124.toChar(), 152.toChar(), 188.toChar(), 200.toChar(), 201.toChar()), charArrayOf(29.toChar(), 57.toChar(), 85.toChar(), 120.toChar(), 148.toChar(), 176.toChar(), 200.toChar(), 201.toChar(), 250.toChar()), charArrayOf(13.toChar(), 48.toChar(), 76.toChar(), 104.toChar(), 132.toChar(), 168.toChar(), 196.toChar(), 200.toChar(), 201.toChar()), charArrayOf(25.toChar(), 60.toChar(), 88.toChar(), 120.toChar(), 148.toChar(), 184.toChar(), 200.toChar(), 201.toChar(), 250.toChar()), charArrayOf(16.toChar(), 44.toChar(), 76.toChar(), 108.toChar(), 144.toChar(), 172.toChar(), 200.toChar(), 201.toChar(), 250.toChar()), charArrayOf(28.toChar(), 60.toChar(), 92.toChar(), 124.toChar(), 160.toChar(), 192.toChar(), 200.toChar(), 201.toChar(), 250.toChar()), charArrayOf(17.toChar(), 53.toChar(), 85.toChar(), 124.toChar(), 156.toChar(), 188.toChar(), 200.toChar(), 201.toChar(), 250.toChar()))
        private val principleTermMap = arrayOf(charArrayOf(21.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 19.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 19.toChar(), 19.toChar(), 20.toChar()), charArrayOf(20.toChar(), 19.toChar(), 19.toChar(), 20.toChar(), 20.toChar(), 19.toChar(), 19.toChar(), 19.toChar(), 19.toChar(), 19.toChar(), 19.toChar(), 19.toChar(), 19.toChar(), 18.toChar(), 19.toChar(), 19.toChar(), 19.toChar(), 18.toChar(), 18.toChar(), 19.toChar(), 19.toChar(), 18.toChar(), 18.toChar(), 18.toChar(), 18.toChar(), 18.toChar(), 18.toChar(), 18.toChar()), charArrayOf(21.toChar(), 21.toChar(), 21.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 19.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 20.toChar()), charArrayOf(20.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 19.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 19.toChar(), 19.toChar(), 20.toChar(), 20.toChar(), 19.toChar(), 19.toChar(), 19.toChar(), 20.toChar(), 20.toChar()), charArrayOf(21.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 20.toChar(), 21.toChar(), 21.toChar()), charArrayOf(22.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 20.toChar(), 20.toChar(), 21.toChar(), 21.toChar(), 21.toChar()), charArrayOf(23.toChar(), 23.toChar(), 24.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 23.toChar()), charArrayOf(23.toChar(), 24.toChar(), 24.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 24.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 23.toChar()), charArrayOf(23.toChar(), 24.toChar(), 24.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 24.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 23.toChar()), charArrayOf(24.toChar(), 24.toChar(), 24.toChar(), 24.toChar(), 23.toChar(), 24.toChar(), 24.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 24.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 24.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 23.toChar()), charArrayOf(23.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 22.toChar(), 22.toChar(), 22.toChar()), charArrayOf(22.toChar(), 22.toChar(), 23.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 23.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 22.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 22.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 22.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 21.toChar(), 22.toChar()))
        private val principleTermYear = arrayOf(charArrayOf(13.toChar(), 45.toChar(), 81.toChar(), 113.toChar(), 149.toChar(), 185.toChar(), 201.toChar()), charArrayOf(21.toChar(), 57.toChar(), 93.toChar(), 125.toChar(), 161.toChar(), 193.toChar(), 201.toChar()), charArrayOf(21.toChar(), 56.toChar(), 88.toChar(), 120.toChar(), 152.toChar(), 188.toChar(), 200.toChar(), 201.toChar()), charArrayOf(21.toChar(), 49.toChar(), 81.toChar(), 116.toChar(), 144.toChar(), 176.toChar(), 200.toChar(), 201.toChar()), charArrayOf(17.toChar(), 49.toChar(), 77.toChar(), 112.toChar(), 140.toChar(), 168.toChar(), 200.toChar(), 201.toChar()), charArrayOf(28.toChar(), 60.toChar(), 88.toChar(), 116.toChar(), 148.toChar(), 180.toChar(), 200.toChar(), 201.toChar()), charArrayOf(25.toChar(), 53.toChar(), 84.toChar(), 112.toChar(), 144.toChar(), 172.toChar(), 200.toChar(), 201.toChar()), charArrayOf(29.toChar(), 57.toChar(), 89.toChar(), 120.toChar(), 148.toChar(), 180.toChar(), 200.toChar(), 201.toChar()), charArrayOf(17.toChar(), 45.toChar(), 73.toChar(), 108.toChar(), 140.toChar(), 168.toChar(), 200.toChar(), 201.toChar()), charArrayOf(28.toChar(), 60.toChar(), 92.toChar(), 124.toChar(), 160.toChar(), 192.toChar(), 200.toChar(), 201.toChar()), charArrayOf(16.toChar(), 44.toChar(), 80.toChar(), 112.toChar(), 148.toChar(), 180.toChar(), 200.toChar(), 201.toChar()), charArrayOf(17.toChar(), 53.toChar(), 88.toChar(), 120.toChar(), 156.toChar(), 188.toChar(), 200.toChar(), 201.toChar()))
        
        val monthNames = arrayOf("一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二")
        
        val chineseMonthNames = arrayOf("正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊")
        val principleTermNames = arrayOf("大寒", "雨水", "春分", "谷雨", "夏满", "夏至", "大暑", "处暑", "秋分", "霜降", "小雪", "冬至")
        val sectionalTermNames = arrayOf("小寒", "立春", "惊蛰", "清明", "立夏", "芒种", "小暑", "立秋", "白露", "寒露", "立冬", "大雪")
        
        @JvmStatic
        fun main(arg: Array<String>) {
            val c = ChineseCalendar()
            var cmd = "day"
            var y = 1901
            var m = 1
            var d = 1
            if (arg.size > 0) cmd = arg[0]
            if (arg.size > 1) y = Integer.parseInt(arg[1])
            if (arg.size > 2) m = Integer.parseInt(arg[2])
            if (arg.size > 3) d = Integer.parseInt(arg[3])
            c.setGregorian(y, m, d)
            c.computeChineseFields()
            c.computeSolarTerms()
            if (cmd.equals("year", ignoreCase = true)) {
                val t = c.yearTable
                for (i in t.indices) println(t[i])
            } else if (cmd.equals("month", ignoreCase = true)) {
                val t = c.monthTable
                for (i in t.indices) println(t[i])
            } else {
                println(c.toString())
            }
        }
        
    }
    
    
    init {
        setGregorian(1901, 1, 1)
    }
    
    fun setGregorian(y: Int, m: Int, d: Int) {
        gregorianYear = y
        gregorianMonth = m
        gregorianDate = d
        isGregorianLeap = isGregorianLeapYear(y)
        dayOfYear = dayOfYear(y, m, d)
        dayOfWeek = dayOfWeek(y, m, d)
        chineseYear = 0
        chineseMonth = 0
        chineseDate = 0
        sectionalTerm = 0
        principleTerm = 0
    }
    
    
    val termString: String
        get() {
            if (gregorianDate == sectionalTerm) {
                return sectionalTermNames[gregorianMonth - 1]
            } else if (gregorianDate == principleTerm) {
                return principleTermNames[gregorianMonth - 1]
            }
            return ""
        }
    
    val chineseMonthString: String
        get() = "${chineseMonthNames[this.chineseMonth - 1]}月"
    
    val chineseDateString: String
        get() {
            val sb = StringBuffer()
            var i = chineseDate
            if (chineseDate >= 30) {
                if (chineseDate == 30) {
                    sb.append("三十")
                } else {
                    sb.append("卅")
                }
                i -= 30
            } else if (chineseDate >= 20) {
                if (chineseDate == 20) {
                    sb.append("二十")
                } else {
                    sb.append("廿")
                }
                i -= 20
            } else if (chineseDate >= 10) {
                sb.append("十")
                i -= 10
            }
            if (i > 0) {
                --i
                sb.append(monthNames[i])
            }
            
            return sb.toString()
        }
    
    val animalName: String
        get() = animalNames[(chineseYear + 1 - 1) % 12]
    
    val dateString: String
        get() {
            val str: String // = "*  /  "
            //var gm = gregorianMonth.toString()
            //if (gm.length == 1) gm = " $gm"
            //var cm = Math.abs(chineseMonth).toString()
            //if (cm.length == 1) cm = " $cm"
            var gd = gregorianDate.toString()
            if (gd.length == 1) gd = " $gd"
            var cd = chineseDate.toString()
            if (cd.length == 1) cd = " $cd"
            str = if (gregorianDate == sectionalTerm) {
                " " + sectionalTermNames[gregorianMonth - 1]
            } else if (gregorianDate == principleTerm) {
                " " + principleTermNames[gregorianMonth - 1]
            } else if (chineseDate == 1 && chineseMonth > 0) {
                " " + chineseMonthNames[chineseMonth - 1] + "月"
            } else if (chineseDate == 1 && chineseMonth < 0) {
                "*" + chineseMonthNames[-chineseMonth - 1] + "月"
            } else {
                " $gd / $cd"
            }
            return str
        }
    
    private fun computeChineseFields(): Int {
        if (gregorianYear < 1901 || gregorianYear > 2100) return 1
        var startYear = baseYear
        var startMonth = baseMonth
        var startDate = baseDate
        chineseYear = baseChineseYear
        chineseMonth = baseChineseMonth
        chineseDate = baseChineseDate
        // 第二个对应日，用以提高计算效率
        // 公历 2000 年 1 月 1 日，对应农历 4697 年 11 月 25 日
        if (gregorianYear >= 2000) {
            startYear = baseYear + 99
            startMonth = 1
            startDate = 1
            chineseYear = baseChineseYear + 99
            chineseMonth = 11
            chineseDate = 25
        }
        var daysDiff = 0
        for (i in startYear until gregorianYear) {
            daysDiff += 365
            if (isGregorianLeapYear(i)) daysDiff += 1 // leap year
        }
        for (i in startMonth until gregorianMonth) {
            daysDiff += daysInGregorianMonth(gregorianYear, i)
        }
        daysDiff += gregorianDate - startDate
        
        chineseDate += daysDiff
        var lastDate = daysInChineseMonth(chineseYear, chineseMonth)
        var nextMonth = nextChineseMonth(chineseYear, chineseMonth)
        while (chineseDate > lastDate) {
            if (Math.abs(nextMonth) < Math.abs(chineseMonth)) chineseYear++
            chineseMonth = nextMonth
            chineseDate -= lastDate
            lastDate = daysInChineseMonth(chineseYear, chineseMonth)
            nextMonth = nextChineseMonth(chineseYear, chineseMonth)
        }
        return 0
    }
    
    private fun computeSolarTerms(): Int {
        if (gregorianYear < 1901 || gregorianYear > 2100) return 1
        sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth)
        principleTerm = principleTerm(gregorianYear, gregorianMonth)
        return 0
    }
    
    override fun toString(): String {
        val buf = StringBuffer()
        buf.append("Gregorian Year: $gregorianYear\n")
        buf.append("Gregorian Month: $gregorianMonth\n")
        buf.append("Gregorian Date: $gregorianDate\n")
        buf.append("Is Leap Year: $isGregorianLeap\n")
        buf.append("Day of Year: $dayOfYear\n")
        buf.append("Day of Week: $dayOfWeek\n")
        buf.append("Chinese Year: $chineseYear\n")
        buf.append("Heavenly Stem: " + (chineseYear - 1) % 10 + "\n")
        buf.append("Earthly Branch: " + (chineseYear - 1) % 12 + "\n")
        buf.append("Chinese Month: $chineseMonth\n")
        buf.append("Chinese Date: $chineseDate\n")
        buf.append("Sectional Term: $sectionalTerm\n")
        buf.append("Principle Term: $principleTerm\n")
        return buf.toString()
    }
    
    // 6*9 + 4
    val yearTable: Array<String>
        get() {
            setGregorian(gregorianYear, 1, 1)
            computeChineseFields()
            computeSolarTerms()
            //val table = arrayOfNulls<String>(58)
            val table = Array<String>(58) { "" }
            table[0] = getTextLine(27, "公历年历：$gregorianYear")
            table[1] = getTextLine(27, "农历年历：" + (chineseYear + 1)
                    + " (" + stemNames[(chineseYear + 1 - 1) % 10]
                    + branchNames[(chineseYear + 1 - 1) % 12]
                    + " - " + animalNames[(chineseYear + 1 - 1) % 12] + "年)")
            var ln = 2
            val blank = ("                                         "
                    + "  " + "                                         ")
            var mLeft: Array<String>?
            var mRight: Array<String>?
            for (i in 1..6) {
                table[ln] = blank
                ln++
                mLeft = monthTable
                mRight = monthTable
                for (j in mLeft.indices) {
                    val line = mLeft[j] + "  " + mRight[j]
                    table[ln] = line
                    ln++
                }
            }
            table[ln] = blank
            ln++
            table[ln] = getTextLine(0, "##/## - 公历日期/农历日期，(*)#月 - (闰)农历月第一天")
            //ln++
            return table
        }
    
    val monthTable: Array<String>
        get() {
            setGregorian(gregorianYear, gregorianMonth, 1)
            computeChineseFields()
            computeSolarTerms()
            //val table = arrayOfNulls<String>(8)
            val table = Array(8) { "" }
            var title: String?
            if (gregorianMonth < 11) {
                title = "                   "
            } else {
                title = "                 "
            }
            title = (title + monthNames[gregorianMonth - 1] + "月" + "                   ")
            val header = "   日    一    二    三    四    五    六 "
            val blank = "                                          "
            table[0] = title
            table[1] = header
            var wk = 2
            var line = ""
            for (i in 1 until dayOfWeek) {
                line += "     " + ' '
            }
            val days = daysInGregorianMonth(gregorianYear, gregorianMonth)
            for (i in gregorianDate..days) {
                line += "$dateString "
                rollUpOneDay()
                if (dayOfWeek == 1) {
                    table[wk] = line
                    line = ""
                    wk++
                }
            }
            for (i in dayOfWeek..7) {
                line += "     " + ' '
            }
            table[wk] = line
            for (i in wk + 1 until table.size) {
                table[i] = blank
            }
            for (i in table.indices) {
                table[i] = table[i].substring(0, table[i].length - 1)
            }
            
            return table
        }
    
    fun rollUpOneDay(): Int {
        dayOfWeek = dayOfWeek % 7 + 1
        dayOfYear++
        gregorianDate++
        var days = daysInGregorianMonth(gregorianYear, gregorianMonth)
        if (gregorianDate > days) {
            gregorianDate = 1
            gregorianMonth++
            if (gregorianMonth > 12) {
                gregorianMonth = 1
                gregorianYear++
                dayOfYear = 1
                isGregorianLeap = isGregorianLeapYear(gregorianYear)
            }
            sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth)
            principleTerm = principleTerm(gregorianYear, gregorianMonth)
        }
        chineseDate++
        days = daysInChineseMonth(chineseYear, chineseMonth)
        if (chineseDate > days) {
            chineseDate = 1
            chineseMonth = nextChineseMonth(chineseYear, chineseMonth)
            if (chineseMonth == 1) chineseYear++
        }
        return 0
    }
    
    fun isGregorianLeapYear(year: Int): Boolean {
        var isLeap = false
        if (year % 4 == 0) isLeap = true
        if (year % 100 == 0) isLeap = false
        if (year % 400 == 0) isLeap = true
        return isLeap
    }
    
    fun daysInGregorianMonth(y: Int, m: Int): Int {
        var d = daysInGregorianMonth[m - 1].toInt()
        if (m == 2 && isGregorianLeapYear(y)) d++ // 公历闰年二月多一天
        return d
    }
    
    fun dayOfYear(y: Int, m: Int, d: Int): Int {
        var c = 0
        for (i in 1 until m) {
            c = c + daysInGregorianMonth(y, i)
        }
        c = c + d
        return c
    }
    
    fun dayOfWeek(y: Int, m: Int, d: Int): Int {
        var yy = y
        var w = 1 // 公历一年一月一日是星期一，所以起始值为星期日
        yy = (yy - 1) % 400 + 1 // 公历星期值分部 400 年循环一次
        var ly = (yy - 1) / 4 // 闰年次数
        ly -= (yy - 1) / 100
        ly += (yy - 1) / 400
        val ry = yy - 1 - ly // 常年次数
        w += ry // 常年星期值增一
        w += 2 * ly // 闰年星期值增二
        w += dayOfYear(yy, m, d)
        w = (w - 1) % 7 + 1
        return w
    }
    
    fun daysInChineseMonth(y: Int, m: Int): Int {
        // 注意：闰月 m < 0
        val index = y - baseChineseYear + baseIndex
        var v: Int
        var l: Int
        var d = 30
        if (m in 1..8) {
            v = chineseMonths[2 * index].toInt()
            l = m - 1
            if (v shr l and 0x01 == 1) d = 29
        } else if (m in 9..12) {
            v = chineseMonths[2 * index + 1].toInt()
            l = m - 9
            if (v shr l and 0x01 == 1) d = 29
        } else {
            v = chineseMonths[2 * index + 1].toInt()
            v = v shr 4 and 0x0F
            if (v != Math.abs(m)) {
                d = 0
            } else {
                d = 29
                for (i in bigLeapMonthYears.indices) {
                    if (bigLeapMonthYears[i] == index) {
                        d = 30
                        break
                    }
                }
            }
        }
        return d
    }
    
    fun nextChineseMonth(y: Int, m: Int): Int {
        var n = Math.abs(m) + 1
        if (m > 0) {
            val index = y - baseChineseYear + baseIndex
            var v = chineseMonths[2 * index + 1].toInt()
            v = v shr 4 and 0x0F
            if (v == m) n = -m
        }
        if (n == 13) n = 1
        return n
    }
    
    fun sectionalTerm(y: Int, m: Int): Int {
        if (y < 1901 || y > 2100) return 0
        var index = 0
        val ry = y - baseYear + 1
        while (ry >= sectionalTermYear[m - 1][index].toInt()) index++
        var term = sectionalTermMap[m - 1][4 * index + ry % 4].toInt()
        if (ry == 121 && m == 4) term = 5
        if (ry == 132 && m == 4) term = 5
        if (ry == 194 && m == 6) term = 6
        return term
    }
    
    fun principleTerm(y: Int, m: Int): Int {
        if (y < 1901 || y > 2100) return 0
        var index = 0
        val ry = y - baseYear + 1
        while (ry >= principleTermYear[m - 1][index].toInt()) index++
        var term = principleTermMap[m - 1][4 * index + ry % 4].toInt()
        if (ry == 171 && m == 3) term = 21
        if (ry == 181 && m == 5) term = 21
        return term
    }
    
    fun getTextLine(s: Int, t: String?): String {
        var str = ("                                         "
                + "  " + "                                         ")
        if (t != null && s < str.length && s + t.length < str.length) {
            str = str.substring(0, s) + t + str.substring(s + t.length)
        }
        return str
    }
    
    // 初始日，公历农历对应日期：
    // 公历 1901 年 1 月 1 日，对应农历 4598 年 11 月 11 日
    private val baseYear = 1901
    private val baseMonth = 1
    private val baseDate = 1
    private val baseIndex = 0
    private val baseChineseYear = 4598 - 1
    private val baseChineseMonth = 11
    private val baseChineseDate = 11
    
    
    var gregorianYear: Int = 0
        private set
    var gregorianMonth: Int = 0
        private set
    var gregorianDate: Int = 0
        private set
    private var isGregorianLeap: Boolean = false
    private var dayOfYear: Int = 0
    private var dayOfWeek: Int = 0 // 周日一星期的第一天
    var chineseYear: Int = 0
        private set
    var chineseMonth: Int = 0
        private set // 负数表示闰月
    var chineseDate: Int = 0
        private set
    var sectionalTerm: Int = 0
        private set
    var principleTerm: Int = 0
        private set
    
}
