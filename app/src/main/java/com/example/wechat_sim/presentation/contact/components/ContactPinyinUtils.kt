package com.example.wechat_sim.presentation.contact.components

import com.example.wechat_sim.data.model.Contact

/**
 * 联系人拼音工具类
 */

// 中文拼音首字母映射表
private val pinyinMap = mapOf(
    // 常见姓氏和名字
    "王" to "W", "李" to "L", "张" to "Z", "刘" to "L", "陈" to "C", "杨" to "Y", "赵" to "Z", "黄" to "H",
    "周" to "Z", "吴" to "W", "徐" to "X", "孙" to "S", "胡" to "H", "朱" to "Z", "高" to "G", "林" to "L",
    "何" to "H", "郭" to "G", "马" to "M", "罗" to "L", "梁" to "L", "宋" to "S", "郑" to "Z", "谢" to "X",
    "韩" to "H", "唐" to "T", "冯" to "F", "于" to "Y", "董" to "D", "萧" to "X", "程" to "C", "曹" to "C",
    "袁" to "Y", "邓" to "D", "许" to "X", "傅" to "F", "沈" to "S", "曾" to "Z", "彭" to "P", "吕" to "L",
    "苏" to "S", "卢" to "L", "蒋" to "J", "蔡" to "C", "贾" to "J", "丁" to "D", "魏" to "W", "薛" to "X",
    "叶" to "Y", "阎" to "Y", "余" to "Y", "潘" to "P", "杜" to "D", "戴" to "D", "夏" to "X", "钟" to "Z",
    "汪" to "W", "田" to "T", "任" to "R", "姜" to "J", "范" to "F", "方" to "F", "石" to "S", "姚" to "Y",
    "谭" to "T", "廖" to "L", "邹" to "Z", "熊" to "X", "金" to "J", "陆" to "L", "郝" to "H", "孔" to "K",
    "崔" to "C", "常" to "C", "康" to "K", "牛" to "N", "龚" to "G", "米" to "M", "温" to "W", "代" to "D",
    "侯" to "H", "顾" to "G", "孟" to "M", "邵" to "S", "龙" to "L", "万" to "W", "段" to "D", "章" to "Z",
    "钱" to "Q", "汤" to "T", "尹" to "Y", "黎" to "L", "易" to "Y", "武" to "W", "乔" to "Q", "贺" to "H",
    "赖" to "L", "文" to "W", "史" to "S", "陶" to "T",

    // 常见字符
    "数" to "S", "学" to "X", "老" to "L", "师" to "S", "大" to "D", "室" to "S", "友" to "Y",
    "同" to "T", "事" to "S", "家" to "J", "庭" to "T", "医" to "Y", "生" to "S", "健" to "J",
    "身" to "S", "教" to "J", "练" to "L", "设" to "S", "计" to "J", "朋" to "P",

    "晨" to "C", "曦" to "X", "梦" to "M", "琪" to "Q", "杰" to "J", "乐" to "L", "思" to "S",
    "雨" to "Y", "云" to "Y", "飞" to "F", "晓" to "X", "萌" to "M", "悦" to "Y", "彤" to "T",
    "浩" to "H", "然" to "R", "诗" to "S", "涵" to "H", "志" to "Z", "豪" to "H", "美" to "M",
    "琳" to "L", "俊" to "J", "雨" to "Y", "轩" to "X", "慧" to "H", "敏" to "M", "晨" to "C",
    "阳" to "Y", "桐" to "T", "远" to "Y", "恒" to "H", "安" to "A", "宇" to "Y", "轩" to "X",
    "泽" to "Z", "涛" to "T", "鑫" to "X", "磊" to "L", "强" to "Q", "军" to "J", "平" to "P",
    "伟" to "W", "刚" to "G", "勇" to "Y", "毅" to "Y", "俊" to "J", "峰" to "F", "超" to "C",
    "越" to "Y", "斌" to "B", "文" to "W", "辉" to "H", "力" to "L", "明" to "M", "永" to "Y",
    "健" to "J", "世" to "S", "广" to "G", "志" to "Z", "义" to "Y", "兴" to "X", "良" to "L",
    "海" to "H", "山" to "S", "仁" to "R", "波" to "B", "宁" to "N", "贵" to "G", "福" to "F",

    // 添加更多常用字符
    "小" to "X", "新" to "X", "中" to "Z", "国" to "G", "华" to "H", "东" to "D", "南" to "N",
    "西" to "X", "北" to "B", "春" to "C", "夏" to "X", "秋" to "Q", "冬" to "D", "青" to "Q",
    "红" to "H", "绿" to "L", "蓝" to "L", "白" to "B", "黑" to "H", "金" to "J", "银" to "Y",
    "天" to "T", "地" to "D", "人" to "R", "和" to "H", "爱" to "A", "情" to "Q", "心" to "X",
    "愿" to "Y", "望" to "W", "信" to "X", "任" to "R", "真" to "Z", "善" to "S", "美" to "M"
)

/**
 * 改进的拼音获取函数
 */
fun getPinyin(text: String): String {
    if (text.isEmpty()) return "#"

    val firstChar = text.first().toString()

    return when {
        // 检查拼音映射表
        pinyinMap.containsKey(firstChar) -> pinyinMap[firstChar]!!
        // 英文字母直接返回大写
        firstChar.first().isLetter() -> firstChar.uppercase()
        // 数字和其他字符返回#
        else -> "#"
    }
}

/**
 * 计算滚动位置的辅助函数
 */
fun calculateScrollPosition(groupedFriends: Map<String, List<Contact>>, targetLetter: String): Int {
    var position = 1 // 从1开始，因为0是功能区

    for ((letter, contacts) in groupedFriends) {
        if (letter == targetLetter) {
            return position
        }
        position += 1 + contacts.size // 1个标题 + N个联系人
    }

    return -1
}