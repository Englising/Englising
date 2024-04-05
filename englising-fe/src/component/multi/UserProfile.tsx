import { User } from "../../pages/MultiplayPage";
import ProfileImage from "./ProfileImage";

interface UserProfileProps {
  classes: string;
  user: User;
}

function UserProfile({ classes, user }: UserProfileProps) {
  return (
    <div className="flex items-center gap-4 text-lg">
      <ProfileImage classes={classes} src={user.profileImg} color={user.color} />
      <p>{user.nickname}</p>
    </div>
  );
}

export default UserProfile;
