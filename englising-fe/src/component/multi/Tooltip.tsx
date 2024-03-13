import { ReactNode } from "react";
import styles from "./Multi.module.css";

type TooltipProps = {
  children: ReactNode;
};

function Tooltip({ children }: TooltipProps) {
  return (
    <div className={`${styles.tooltip} absolute z-10 -bottom-12 left-3 -mx-24 p-2 rounded-lg bg-black text-sm`}>
      {children}
    </div>
  );
}

export default Tooltip;
