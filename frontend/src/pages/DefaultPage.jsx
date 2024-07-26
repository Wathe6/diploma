import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Flex, Box, Text } from '@chakra-ui/react';
import StudentsTable from "../components/StudentsTable.jsx";
import TeachersTable from "../components/TeachersTable.jsx";
import NavigationBar from "../components/NavigationBar.jsx";
import ProfileTable from "../components/ProfileTable.jsx";

const DefaultPage = () => {
    const [data, setData] = useState([]);
    const [userRole, setUserRole] = useState('');
    const [selected, setSelected] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/user-info');
                setUserRole(response.data.user);
                console.log(response.data)
                setData(response.data.info);
            } catch (error) {
                console.error('Error fetching data: ', error);
            }
        };

        fetchData();
    }, []);

    return (
        <Flex width="100%" height="100vh">
            <Box
                position="sticky"
                top="0"
                height="100vh"
                overflowY="auto"
                flexShrink={0}
            >
                <NavigationBar data={data} setSelected={setSelected} />
            </Box>
            <Flex
                flex="1"
                justifyContent="center"
                overflowY="auto"
                padding="1rem"
            >
                {selected === 'profile' ? (
                    <ProfileTable/>
                ) : userRole === 'student' ? (
                    <StudentsTable data={data} selected={selected} />
                ) : userRole === 'teacher' ? (
                    <TeachersTable data={data} selected={selected} />
                ) : (
                    <Text>Loading...</Text>
                )}
            </Flex>
        </Flex>
    );
};

export default DefaultPage;
