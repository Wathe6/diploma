import React from 'react';
import {Flex, Button, Center, Box, Text} from '@chakra-ui/react';
import ToggleThemeButton from "./ToggleThemeButton.jsx";

const NavigationBar = ({ data, setSelected }) => {
    const handleClick = (idSubject) => {
        setSelected(idSubject);
    };

    return (
        <Flex direction="column" align="center" position="relative" height="100vh" width="100%">
            <Box position="absolute" top="0" width="100%">
                <Button onClick={() => handleClick('profile')} width="100%">
                    Профиль
                </Button>
            </Box>
            <Center flexDir="column" alignItems="stretch" flex="1" width="100%">
                {data.map((item, index) => (
                    <Button key={index} mb={2} onClick={() => handleClick(item.idSubject)} width="100%">
                        {item.subjectName}
                    </Button>
                ))}
            </Center>
            <Box position="absolute" bottom="0" width="100%">
                <Text align="center" style={{ width: "100%"}} >Создано ст. гр. ИИ-20</Text>
                <Text align="center" style={{ width: "100%"}} >Пустовым В. А.</Text>
                <ToggleThemeButton />
            </Box>
        </Flex>
    );
};
export default NavigationBar;
