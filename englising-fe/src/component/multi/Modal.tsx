import { ForwardedRef, ReactNode, forwardRef } from "react";

type ModalProps = {
  children: ReactNode;
};

const Modal = forwardRef(function Modal({ children }: ModalProps, ref: ForwardedRef<HTMLDialogElement>) {
  return (
    <dialog className="bg-gray-800 text-white rounded-xl" ref={ref}>
      {children}
    </dialog>
  );
});

export default Modal;
