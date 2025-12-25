# AutoWechat 数据结构文档

## 数据模型类 (model/)

### Contact - 联系人数据类

微信联系人的完整信息模型，用于存储用户的基本信息和社交关系。

**字段说明：**
- `userId: String` - 用户唯一标识符（eg, user_1）
- `userName: String` - 用户显示名称（eg, 王晨曦）
- `remarkName: String?` - 备注名称，好友专有（eg, 大学室友）
- `wxId: String?` - 微信号（eg, wangchenxi_2024）
- `region: String?` - 地理位置，省市格式（eg, 北京 朝阳）
- `avatarUrl: String?` - 头像文件路径（eg, avatar/friend_1.jpg）
- `signature: String?` - 个性签名（eg, 生活不止眼前的苟且，还有诗和远方）
- `isFriend: Boolean` - 好友关系标识（eg, true）

### Message - 消息数据类

微信聊天消息的数据模型，支持私聊和群聊场景。

**字段说明：**
- `id: String` - 消息唯一标识符（eg, msg_001）
- `senderId: String` - 发送者用户ID（eg, user_1）
- `receiverId: String?` - 接收者ID，私聊使用（eg, user_2）
- `chatRoomId: String?` - 群聊ID，群聊使用（eg, group_1）
- `content: String` - 消息文本内容（eg, 今天天气不错啊）
- `type: MessageType` - 消息类型枚举（eg, MessageType.TEXT）
- `timestamp: LocalDateTime` - 消息发送时间（eg, 2025-09-15T10:30:00）
- `isFromSelf: Boolean` - 是否自己发送（eg, false）
- `mediaPath: String?` - 媒体文件路径（eg, images/photo_001.jpg）

**MessageType枚举：**
- `TEXT` - 文本消息
- `IMAGE` - 图片消息
- `VOICE` - 语音消息
- `VIDEO` - 视频消息
- `FILE` - 文件消息
- `EMOJI` - 表情消息
- `LOCATION` - 位置消息
- `SYSTEM` - 系统消息（群成员变动等）

### ChatRoom - 群聊数据类

微信群聊的基本信息和成员管理模型。

**字段说明：**
- `chatRoomId: String` - 群聊唯一标识符（eg, group_1）
- `name: String` - 群聊显示名称（eg, 家庭群）
- `memberIds: List<String>` - 成员用户ID列表（eg, ["current_user", "user_1", "user_2"]）
- `avatarUrl: String?` - 群头像文件路径（eg, avatar/group_1.jpg）
- `announcement: String?` - 群公告内容（eg, 家人闲聊，分享生活点滴）
- `isActive: Boolean` - 群聊活跃状态（eg, true）

### MomentsPost - 朋友圈动态数据类

微信朋友圈动态的完整数据模型，包含内容、互动信息。

**字段说明：**
- `postId: String` - 动态唯一标识符（eg, post_1）
- `authorId: String` - 发布者用户ID（eg, user_1）
- `content: String?` - 文字内容（eg, 今天天气真不错，出门散步心情很好！）
- `images: List<String>` - 图片URL列表（eg, ["image_1.jpg", "image_2.jpg"]）
- `timestamp: LocalDateTime` - 发布时间（eg, 2025-09-15T10:30:00）
- `likes: MutableList<String>` - 点赞用户ID列表（eg, ["current_user", "user_2"]）
- `comments: MutableList<Comment>` - 评论列表（eg, [Comment对象]）
- `isVisible: Boolean` - 可见性状态（eg, true）

### Comment - 朋友圈评论数据类

朋友圈动态下的评论信息模型，支持评论回复。

**字段说明：**
- `commentId: String` - 评论唯一标识符（eg, comment_1）
- `authorId: String` - 评论者用户ID（eg, user_2）
- `content: String` - 评论文本内容（eg, 是啊，适合出游）
- `timestamp: LocalDateTime` - 评论时间（eg, 2025-09-15T11:30:00）
- `replyToId: String?` - 回复目标评论ID（eg, comment_1）

### AppConfig - 应用配置数据类

应用全局配置和状态管理的根数据模型。

**字段说明：**
- `currentUserId: String` - 当前登录用户ID（eg, current_user）
- `contacts: List<Contact>` - 所有联系人列表（eg, [Contact对象列表]）
- `chatRooms: List<ChatRoom>` - 所有群聊列表（eg, [ChatRoom对象列表]）
- `momentsData: MomentsData` - 朋友圈数据容器（eg, MomentsData对象）
- `messages: List<Message>` - 所有消息记录（eg, [Message对象列表]）

### MomentsData - 朋友圈数据容器

朋友圈相关数据的组织容器。

**字段说明：**
- `posts: List<MomentsPost>` - 所有朋友圈动态（eg, [MomentsPost对象列表]）

## 初始化数据 (data/)

### contacts.json - 联系人初始数据

包含完整的用户关系网络数据，区分好友和非好友两类用户。

**数据结构：**
- `friends` - 好友列表（8人）
  - 每个好友都有备注名（remarkName）
  - 头像文件命名：`avatar/friend_1.jpg` 到 `avatar/friend_8.jpg`
  - 地区格式：省市格式（如"北京 朝阳"、"湖北 武汉"）

- `nonFriends` - 非好友列表（10人）
  - 无备注名（remarkName为null）
  - 头像文件命名：`avatar/nonfriend_1.jpg` 到 `avatar/nonfriend_10.jpg`
  - 同样使用省市格式的地区信息

### chatrooms.json - 群聊初始数据

预设的群聊信息，模拟真实的社交群组场景。

**群聊列表：**
1. **家庭群** - 5名成员（1个非好友）
2. **工作讨论组** - 8名成员（5个非好友）
3. **大学同学群** - 7名成员（3个非好友）
4. **健身打卡群** - 5名成员（1个非好友）

每个群聊包含：
- 群头像：`avatar/group_1.jpg` 到 `avatar/group_4.jpg`
- 群公告和成员混合配置（好友+非好友）

### moments.json - 朋友圈初始数据

预设的朋友圈动态数据，包含完整的社交互动信息。

**包含内容：**
- 6条不同用户发布的动态
- 每条动态包含点赞和评论数据
- 时间分布在过去几天
- 涵盖动物主题的图片内容

**具体动态：**
1. **post_1** - 王晨曦：橘猫睡觉图片 (`post_1_cat_sleeping.jpg`)
2. **post_2** - 张杰：牧羊犬奔跑图片 (`post_2_dog_running.jpg`)
3. **post_3** - 赵晓萌：动物漫画图片 (`post_3_animal_comic.jpg`)
4. **post_4** - 何俊杰：天鹅夕阳图片 (`post_4_swan_sunset.jpg`)
5. **post_5** - 陈思雨：蜜蜂采花图片 (`post_5_bee_flower.jpg`)
6. **post_6** - 黄诗涵：可爱熊猫图片 (`post_6_panda_cute.jpg`)

### messages.json - 聊天记录初始数据

按聊天对象分类的消息数据，包含私聊和群聊记录。

**数据结构：**
- `privateChatMessages` - 私聊记录（8个好友）
  - 每个好友10-12轮对话，共122条消息
  - 包含文本和图片消息
  - 时间分布在9月15-16日

- `groupChatMessages` - 群聊记录（4个群）
  - 每个群20+条消息，共66条消息
  - 覆盖所有群成员参与
  - 包含图片分享和丰富的群聊互动

**消息分布：**
- **总消息数量**：188条消息（122条私聊 + 66条群聊）
- **消息ID范围**：msg_001 到 msg_188
- 文本消息为主，部分图片消息
- 正确设置isFromSelf字段（区分自己和他人发送）
- 使用现有image资源（image_3.jpg到image_5.jpg）
- 包含emoji表情，增加对话真实感

**群聊特色内容：**
- **家庭群**：家庭出游计划，温馨交流
- **工作讨论组**：项目会议安排，团队协作
- **大学同学群**：同学聚会策划，回忆青春
- **健身打卡群**：运动打卡，健身房聚会

### app_config.json - 应用配置数据

应用的全局配置信息。

**配置内容：**
- 当前用户信息（使用头像 `avatar/me.jpg`）
- 当前用户基本资料（用户名、微信号、地区、签名等）

## 资源文件组织

### 头像文件 (assets/avatar/)

头像文件按功能分类，便于管理和识别：

- `me.jpg` - 当前用户头像
- `friend_1.jpg` ~ `friend_8.jpg` - 好友头像（8个）
- `nonfriend_1.jpg` ~ `nonfriend_10.jpg` - 非好友头像（10个）
- `group_1.jpg` ~ `group_4.jpg` - 群聊头像（4个）

### 朋友圈图片 (assets/image/)

**朋友圈图片 (assets/image/)：**
- `post_1_cat_sleeping.jpg` - 橘猫睡觉图
- `post_2_dog_running.jpg` - 牧羊犬奔跑图
- `post_3_animal_comic.jpg` - 动物世界漫画图
- `post_4_swan_sunset.jpg` - 天鹅夕阳图
- `post_5_bee_flower.jpg` - 蜜蜂采花图
- `post_6_panda_cute.jpg` - 可爱熊猫图

**聊天图片 (assets/image/)：**
- `image_3.jpg` - 私聊中使用
- `image_4.jpg` - 家庭群中使用
- `image_5.jpg` - 健身群中使用

**未使用图片：**
- `image_6.jpg` ~ `image_29.jpg` - 保持原文件名，供后续扩展使用

所有资源文件在代码中使用相对路径引用（如 `avatar/me.jpg`、`image/post_1_cat_sleeping.jpg`）。