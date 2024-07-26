import React, { useState } from "react";
import {Box, Divider, Flex} from "@chakra-ui/react";
import AdminTable from "../components/AdminTable.jsx";

const AdminPage = () => {
    const [dividerPosition, setDividerPosition] = useState(50); // начальная позиция Divider

    const handleMouseDown = (e) => {
        e.preventDefault();
        document.addEventListener("mousemove", handleMouseMove);
        document.addEventListener("mouseup", handleMouseUp);
    };

    const handleMouseMove = (e) => {
        const newDividerPosition = e.clientX / window.innerWidth * 100;
        setDividerPosition(newDividerPosition);
    };

    const handleMouseUp = () => {
        document.removeEventListener("mousemove", handleMouseMove);
        document.removeEventListener("mouseup", handleMouseUp);
    };
    return (
        <Flex justifyContent="center">
            <Box flex={`0 0 ${dividerPosition}%`} display="flex"  justifyContent="center">
                <AdminTable />
            </Box>
            <Divider
                orientation="vertical"
                height="100vh"
                borderWidth="4px"
                cursor="col-resize"
                onMouseDown={handleMouseDown}
            />
            <Box flex={`0 0 ${100 - dividerPosition}%`} display="flex" justifyContent="center">
                <AdminTable />
            </Box>
        </Flex>
    );
}

export default AdminPage;