import React, { useState } from 'react';
import { Box, Select, Table, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react";

const TeacherProfile = ({ data }) => {
    const [selectedSubject, setSelectedSubject] = useState(Object.keys(data.info)[0]);

    const handleSubjectChange = (event) => {
        setSelectedSubject(event.target.value);
    };

    const { works, grades } = data.info[selectedSubject];

    const students = Object.keys(grades);

    return (
        <Box>
            <Select onChange={handleSubjectChange} value={selectedSubject} mb={4} style={{fontSize: "28px"}}>
                {Object.keys(data.info).map((subject, index) => (
                    <option key={index} value={subject} style={{fontSize: "28px"}}>{subject}</option>
                ))}
            </Select>
            <Table className="chakra-table">
                <Thead>
                    <Tr>
                        <Th rowSpan="2" className="th">ФИО</Th>
                        {works.map((work, index) => (
                            <Th key={index} className="th">{work}</Th>
                        ))}
                    </Tr>
                </Thead>
                <Tbody>
                    {students.map((student, index) => (
                        <Tr key={index}>
                            <Td className="td">{student}</Td>
                            {grades[student].map((grade, gradeIndex) => (
                                <Td key={gradeIndex} className="td">{grade}</Td>
                            ))}
                        </Tr>
                    ))}
                </Tbody>
            </Table>
        </Box>
    );
};

export default TeacherProfile;