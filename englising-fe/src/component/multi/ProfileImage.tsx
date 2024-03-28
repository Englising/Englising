interface ProfileImageProps {
  classes: string;
  src: string;
}

function ProfileImage({ classes, src }: ProfileImageProps) {
  return (
    <div className={`${classes} rounded-full overflow-hidden`}>
      <img src={src} />
    </div>
  );
}

export default ProfileImage;
