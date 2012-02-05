import Data.UUID
import System.UUID.V1
import Control.Monad
import Data.Bson
import System.IO
import System.Random
import Data.Maybe
import Data.Time
import Data.Time.Format
import Data.Time.Clock
import Data.Time.Calendar
import Data.Time.Clock.POSIX
import Database.HDBC
import Database.HDBC.MySQL
import Database.MongoDB

data Group = Group{
  groupId :: String,
  groupName :: String,
  groupProfilePicId:: String,
  description::String
}deriving (Show)

data Post = Post{
  postId :: String,
  text :: String,
  imageId :: String,
  latitude :: Double,
  longitude :: Double,
  postedGroup:: String,
  inReplyTo :: Maybe String, 
  comments :: Maybe [String], 
  postUserId :: String, 
  postTimestamp :: Int
} deriving (Show)

data User = User{
  userId :: String,
  name :: String,
  email :: String,
  points :: Int,
  credits :: Int,
  fbToken :: String,
  profilePicId :: String,
  userGroupId:: String,
  posts:: Maybe [String]
} deriving (Show)



generateUUID = do 
  uuid_raw <- (liftM show uuid)
  return [x|x<- uuid_raw, x /='-']

generateObjectId = do
  rawObjectId <- liftM show genObjectId 
  liftM show genObjectId

generateGroup :: Int -> IO Main.Group
generateGroup count = do
  groupId <- generateUUID
  groupProfilePicId <- generateObjectId
  let groupName = "Frank Fantastic Group" ++ groupId
      description = "Frank Awesome Group" ++ groupId
  return Main.Group{groupId = groupId, groupName = groupName, groupProfilePicId = groupProfilePicId, description = description}

generatePost :: String -> String -> Int ->  IO Post
generatePost groupId userId count= do
  postId <- generateUUID
  imageId <- generateObjectId
  latitude <- randomRIO (-90.0, 90.0)
  longitude <- randomRIO (-180.0, 180)
  currentTime <-getCurrentTime
  let text = "Hello I am Frank"
      inReplyTo  = Nothing
      comments = Nothing
      timestamp = ceiling $ toRational $ utcTimeToPOSIXSeconds currentTime
  return Post{ postId = postId, text = text, imageId = imageId, latitude = latitude, longitude = longitude, postedGroup = groupId, inReplyTo = inReplyTo, comments = comments, postUserId = userId, postTimestamp = timestamp}


generateUser:: String -> Int-> IO User
generateUser groupId count= do
  id <- generateUUID
  profilePicId <- generateObjectId
  fbToken <- generateUUID
  points <- randomRIO (1, 100)
  credits <- randomRIO (1, 100)
  let name = "Frank"++id
      email = "frank"++id++"@email.com" 
      posts  = Nothing

  return  User{ userId= id, profilePicId = profilePicId, name = name, email = email, points = points, credits = credits, fbToken = fbToken, userGroupId= groupId, posts = posts}

randomItemFromList :: [a] -> a
randomItemFromList list = 
 list !! (head $ take 1 $ randomRs (0, (length list)) (mkStdGen 3))


convertPostIntoDocument :: Post -> Document
convertPostIntoDocument post =
  let doc = ["_id" =: (postId post), "text" =: (text post), "image_id" =: (imageId post), "latitude" =: (latitude post), "longitude" =: (longitude post), "group_id" =: (postedGroup post), "user_id" =: (postUserId post), "timestamp" =: (postTimestamp post) ]
  in addCommentsToDocument (addMainPostIdToDocument doc post ) post

addMainPostIdToDocument :: Document  -> Post -> Document
addMainPostIdToDocument document post = 
  if isNothing (inReplyTo post)
  then document
  else document ++ ["main_post_id" =: (fromJust $ inReplyTo post)]

addCommentsToDocument :: Document  -> Post -> Document
addCommentsToDocument document post = 
  if isNothing (comments post)
  then document
  else document ++ ["comment_ids" =: (fromJust $ comments post) ]

updateUserPosts :: Connection -> Post -> IO Integer
updateUserPosts connection post = do
  currentTime <- getCurrentTime
  insert<- prepare connection "insert into users_posts(user_id, post_id, group_id, created_at, updated_at) values (?, ?, ?, ?, ?)"
  row <- execute insert [ toSql (postUserId post), toSql (postId post), toSql (postedGroup post), toSql currentTime, toSql currentTime ]
  finish insert
  commit connection
  return row

insertGroupsIntoDb connection group= do
  currentTime <- getCurrentTime
  insert <- prepare connection "insert into groups(id, name, profile_pic_id, description, created_at, updated_at) values(?, ?, ?, ?, ?, ?)"
  row <- execute insert [toSql (groupId group), toSql (groupName group), toSql (groupProfilePicId group), toSql (description group), toSql currentTime, toSql currentTime]
  finish insert
  commit connection
  return row


insertUsersIntoDb :: Connection -> User -> IO Integer
insertUsersIntoDb connection user = do
  currentTime <- getCurrentTime
  insert <- prepare connection "insert into users(id, name, email, profile_pic_id, points, credits, fb_token, created_at, updated_at) values(?, ?, ?, ?, ?, ?, ?, ?, ?)"
  row <- execute insert [toSql (userId user), toSql (name user), toSql (email user), toSql (profilePicId user), toSql (points user), toSql (credits user), toSql (fbToken user), toSql currentTime, toSql currentTime]
  finish insert
  insertUserGroup <- prepare connection "insert into users_groups(user_id, group_id, created_at, updated_at) values (?, ?, ?, ?)"
  rowUserGroup <- execute insertUserGroup [toSql (userId user), toSql (userGroupId user), toSql currentTime, toSql currentTime]
  finish insertUserGroup
  commit connection
  return (row + rowUserGroup)

insertPostsIntoDb :: Connection -> [Post] ->  IO ()
insertPostsIntoDb  connection posts = do
  pipe <- runIOE $ connect (host "127.0.0.1")
  rows <- forM posts (updateUserPosts connection)
  let docs = map convertPostIntoDocument posts
  e <- access pipe master "symfluence" (insertMany "posts" docs)
  close pipe
  print e
  return ()




generateAll = do
  groups <- sequence (map generateGroup [1..10])
  users <- sequence (map (generateUser (groupId $ randomItemFromList groups)) [1..10])
  posts <- sequence(map (generatePost (groupId $ randomItemFromList groups) (userId $ randomItemFromList users)) [1..10])
  connection <- connectMySQL defaultMySQLConnectInfo {
                 mysqlHost = "127.0.0.1", 
                 mysqlUser="root", 
                 mysqlPassword="",
                 mysqlPort=3306, 
                 mysqlDatabase="symfluence"
              }
  forM groups  (insertGroupsIntoDb connection)
  forM users (insertUsersIntoDb connection)
  insertPostsIntoDb connection posts
  print groups
  print users
  print posts
  
  return ()


