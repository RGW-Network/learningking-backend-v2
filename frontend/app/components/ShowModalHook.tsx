import { useState } from "react";

export default function useShowModalDialog() {
    const [openDialog, setOpenDialog] = useState(false);

    const toggleOpenDialog = () => {
        setOpenDialog(!openDialog);
    };

    return {
        openDialog,
        toggleOpenDialog
    };
};