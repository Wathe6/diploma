import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Box } from "@chakra-ui/react";
import StudentProfile from "./profiles/StudentProfile.jsx";
import HeadmanProfile from "./profiles/HeadmanProfile.jsx";
import TeacherProfile from "./profiles/TeacherProfile.jsx";
import HeadProfile from "./profiles/HeadProfile.jsx";

const ProfileTable = () => {
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('/profile');
                setData(response.data);
            } catch (error) {
                setError('Error fetching data');
            }
        };

        fetchData();
    }, []);

    if (error) {
        return <Box>{error}</Box>;
    }

    if (!data) {
        return <Box>Loading...</Box>;
    }

    const { user } = data;

    switch (user) {
        case 'student':
            return <Box><StudentProfile data={data} /></Box>;
        case 'headman':
            return <Box><HeadmanProfile data={data} /></Box>;
        case 'teacher':
            return <Box><TeacherProfile data={data} /></Box>;
        case 'head':
            return <Box><HeadProfile data={data} /></Box>;
        default:
            return <Box>Unknown user type</Box>;
    }
};

export default ProfileTable;
