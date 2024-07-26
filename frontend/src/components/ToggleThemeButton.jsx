import { Button, useColorMode } from "@chakra-ui/react";

const ToggleThemeButton = () => {
    const { colorMode, toggleColorMode } = useColorMode();

    return (
        <Button onClick={toggleColorMode} style={{ width: "100%"}} >
            {colorMode === "light" ? "Темная тема" : "Светлая тема"}
        </Button>
    );
};

export default ToggleThemeButton;
