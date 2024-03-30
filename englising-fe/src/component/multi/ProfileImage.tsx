interface ProfileImageProps {
  classes: string;
  src: string;
  color?: string;
}

function ProfileImage({ classes, src, color }: ProfileImageProps) {
  return (
    <div className={`${classes} rounded-full overflow-hidden`} style={{ backgroundColor: `${color}` }}>
      <img src={src} />
    </div>
  );
}

export default ProfileImage;
