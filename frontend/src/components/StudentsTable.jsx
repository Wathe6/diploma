import React, {useMemo, useRef, useState} from 'react';
import {getCoreRowModel, useReactTable} from '@tanstack/react-table';
import axios from 'axios';
import {
    Box,
    Text,
    Button,
    Input,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalCloseButton, ModalBody, useDisclosure, ModalFooter
} from '@chakra-ui/react';
import ViewTable from "../components/ViewTable.jsx";

const columnsWorks = [
    {
        header: <Text>Работы</Text>,
        accessorKey: "work.worktype",
        size: 150,
        cell: (props) => <Text>{props.getValue()}</Text>
    },{
        header: <Text>Дата открытия</Text>,
        accessorKey: "work.dateOpen",
        size: 230,
        cell: (props) => <Text>{props.getValue()}</Text>
    },{
        header: <Text>Дата окончания</Text>,
        accessorKey: "work.dateEnd",
        size: 230,
        cell: (props) => <Text>{props.getValue()}</Text>
    },{
        header: <Text>Статус</Text>,
        accessorKey: "grade.status",
        size: 220,
        cell: (props) => {
            const { isOpen, onOpen, onClose } = useDisclosure();

            const handleClick = () => {
                onOpen();
            };

            const activity = props.row.original.grade.activity;

            return (
                <>
                    <Text onClick={handleClick} cursor="pointer">{props.getValue()}</Text>
                    <Modal isOpen={isOpen} onClose={onClose}>
                        <ModalOverlay />
                        <ModalContent maxWidth="50vw" maxHeight="50vh">
                            <ModalHeader>Статус</ModalHeader>
                            <ModalCloseButton />
                            <ModalBody>
                                {Array.isArray(activity) ? activity.map((item, index) => (
                                    <Text key={index}>{item}</Text>
                                )) : null}
                            </ModalBody>
                            <ModalFooter>
                                <Button colorScheme="blue" onClick={onClose}>
                                    Закрыть
                                </Button>
                            </ModalFooter>
                        </ModalContent>
                    </Modal>
                </>
            );
        }
    },{
        header: <Text>Оценка</Text>,
        accessorKey: "grade.grade",
        size: 130,
        cell: (props) => <Text>{props.getValue()}</Text>
    },{
        accessorKey: "work.reference",
        cell: props => {
            const handleGet = async (props) => {
                try {
                    // Получение URL и извлечение параметра страницы, если он существует
                    const url = `/docs${props.getValue()}`;
                    const pageParam = new URLSearchParams(url.split('?')[1]).get('page');

                    // Запрос к серверу для получения файла
                    const response = await axios.get(url.split('?')[0], {
                        responseType: 'blob' // Важно, чтобы axios вернул данные как Blob
                    });

                    // Создание временного URL для Blob данных
                    const fileURL = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));

                    // Открытие PDF файла в новой вкладке
                    window.open(fileURL, '_blank');
                } catch (error) {
                    console.error('Error:', error);
                }
            };

            return (
                <Button
                    colorScheme="teal"
                    size="lg" m={0.5} fontSize="28px"
                    onClick={() => handleGet(props)}>
                    Открыть
                </Button>
            );
        }
    },{
        accessorKey: "grade.reference",
        cell: props => {
            const fileInputRef = useRef(null);

            const handleButtonClick = (props) => {
                fileInputRef.current.click();
                fileInputRef.current.onchange = (event) => handleFileChange(event, props);
            };

            const handleFileChange = async (event, props) => {
                const selectedFile = event.target.files[0];
                if (!selectedFile) {
                    console.error("No file selected");
                    return;
                }

                const formData = new FormData();
                formData.append('file', selectedFile);
                formData.append('props', JSON.stringify(props.grade));
                console.log("props", props)
                console.log("formData", formData)
                try {
                    const response = await axios.post(`/works${props.grade.reference}`, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                    console.log('Response:', response);
                } catch (error) {
                    console.error('Error:', error);
                }
            };

            return (
                <>
                    <Input
                        type="file"
                        accept="application/pdf"
                        ref={fileInputRef}
                        style={{ display: 'none' }}
                    />
                    <Button
                        colorScheme="green"
                        onClick={() => handleButtonClick(props.row.original)}
                        size="lg" m={0.5} fontSize="28px"
                        isDisabled={props.row.original.grade.status === "Выполнена"}
                    >
                        Отправить
                    </Button>
                </>
            );
        }
    }
];
const columnsTeachers = [
    {
        header: <Text>ФИО</Text>,
        accessorKey: "teacher.name",
        size: 570,
        cell: (props) => <Text>{props.getValue()}</Text>
    }, {
        header: <Text>Должность</Text>,
        accessorKey: "teacher.position",
        size: 250,
        cell: (props) => <Text>{props.getValue()}</Text>
    },{
        header: <Text>Уч. степень</Text>,
        accessorKey: "teacher.academicdegree",
        size: 220,
        cell: (props) => <Text>{props.getValue() === "NULL" ? "" : props.getValue()}</Text>
    },{
        header: <Text>Уч. звание</Text>,
        accessorKey: "teacher.academictitle",
        size: 220,
        cell: (props) => <Text>{props.getValue() === "NULL" ? "" : props.getValue()}</Text>
    }
];

const StudentsTable = ({ data, selected }) => {
    const findSubjectById = (idSubject) => {
        return data.find((item) => item.idSubject === idSubject);
    };
    const subject = findSubjectById(selected);

    if (!subject) {
        return <Text>Выберите программу</Text>;
    }
    const gradeWork = useMemo(() => {
        return subject.gradeWork.map(item => {
            const updatedWork = {
                ...item.work,
                worktype: `${item.work.worktype} ${item.work.number}`
            };
            return {
                ...item,
                work: updatedWork
            };
        });
    }, [subject]);

    const tableWorks = useReactTable({
        data: gradeWork,
        columns: columnsWorks,
        getCoreRowModel: getCoreRowModel()
    });

    const teachers = useMemo(() => {
        return subject.teachers.map(item => {
            const updatedTeacher = {
                ...item.teacher,
                name: `${item.teacher.surname} ${item.teacher.name} ${item.teacher.patronymic}`
            };
            return {
                ...item,
                teacher: updatedTeacher
            }
        })
    }, [subject]);

    const tableTeachers = useReactTable({
        data: teachers,
        columns: columnsTeachers,
        getCoreRowModel: getCoreRowModel()
    })

    return (
        <Box>
            <Box>
                <Text>{subject.subjectName + " (" + subject.abbreviation + ")"}</Text>
            </Box>
            <ViewTable table={tableTeachers}/>
            <Box height="14pt"/>
            <ViewTable table={tableWorks}/>
        </Box>
    );
};

export default StudentsTable;
