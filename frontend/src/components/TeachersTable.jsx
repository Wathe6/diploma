import React, { useMemo, useState, useEffect } from 'react';
import {
    Box,
    Button,
    Icon, Input,
    Modal, ModalBody,
    ModalCloseButton,
    ModalContent, ModalFooter,
    ModalHeader,
    ModalOverlay,
    Text, Textarea,
    useDisclosure
} from '@chakra-ui/react';
import axios from 'axios';
import { getCoreRowModel, useReactTable } from '@tanstack/react-table';
import ViewTable from './ViewTable.jsx';
import TeacherEditableTable from './TeachersEditableTable.jsx';
import CheckIcon from './icons/CheckIcon.jsx';
import EditableCell from './EditableCell.jsx';
import HamburgerIcon from "./icons/HamburgerIcon.jsx";
import CloseIcon from "./icons/CloseIcon.jsx";


const TeachersTable = ({ data, selected }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [sgwdtos, setSgwdtos] = useState([]);
    const subject = data.find((item) => item.idSubject === selected);

    const columns = [
        {
            header: <Text>Студент</Text>,
            accessorKey: "fio",
            size: 400,
            cell: (props) => <Text>{props.getValue()}</Text>
        },
        {
            header: <Text>Дата окончания</Text>,
            accessorKey: "work.dateEnd",
            size: 220,
            cell: (props) => <Text>{props.getValue()}</Text>
        },
        {
            header: <Text>Задание</Text>,
            accessorKey: "work.worktype",
            size: 150,
            cell: (props) => <Text>{props.getValue()}</Text>
        },
        {
            header: <Text>Статус</Text>,
            accessorKey: "grade.status",
            size: 200,
            cell:(props) => <Text>{props.getValue()}</Text>
        },
        {
            header: <Text>Оценка</Text>,
            accessorKey: "grade.grade",
            size: 130,
            cell: EditableCell
        },
        {
            accessorKey: "work.idWork",
            size: 192,
            cell: (props) => {
                const [grade, setGrade] = useState(props.row.original.grade_grade);
                const { isOpen, onOpen, onClose } = useDisclosure();
                const [inputValue, setInputValue] = useState('');

                const handleCheck = async () => {
                    if (grade !== props.row.original.grade.grade) {
                        console.log("Изменилось ", grade, " ", props.row.original.grade.grade)
                        try {
                            const response = await axios.post(`/grade/${props.row.original.grade.idStudent}/${props.row.original.work.idWork}/${grade}`);
                            console.log('Response:', response);

                            // Обновляем оценку в состоянии
                            setGrade(grade);

                            // Обновляем статус в таблице
                            const updatedGrade = {
                                ...props.row.original.grade,
                                grade: grade,
                                status: 'Выполнена'
                            };
                            updateGrade(updatedGrade);

                        } catch (error) {
                            console.error('Error:', error);
                        }
                    } else {
                        console.log("Не изменилось", grade, props.row.original.grade.grade)
                    }
                };

                const handleRedButtonClick = () => {
                    onOpen();
                };

                const handleInputChange = (event) => {
                    setInputValue(event.target.value);
                };

                const handleSubmit = async () => {
                    const currentTime = new Date().toLocaleString('ru-RU', {
                        day: '2-digit', month: '2-digit', year: 'numeric',
                        hour: '2-digit', minute: '2-digit'
                    });
                    const newActivityEntry = `[${currentTime}] ${inputValue}`;

                    // Сохранение существующих записей в activity или создание нового массива
                    const existingActivity = props.row.original.grade.activity || [];

                    // Добавление новой записи к существующим
                    const updatedActivity = [...existingActivity, newActivityEntry];

                    const updatedGrade = {
                        ...props.row.original.grade,
                        activity: updatedActivity,
                        status: 'Замечания'
                    };

                    try {
                        console.log("Server POST: ", [updatedGrade]);
                        await axios.post('/api/grades', [updatedGrade]);

                        updateGrade(updatedGrade);

                        onClose();
                    } catch (error) {
                        console.error('Error:', error);
                    }
                };

                return (
                    <Box>
                        <Button
                            colorScheme="green"
                            onClick={handleCheck}
                            size="lg" m={0.5} fontSize="28px"
                            title="Поставить оценку">
                            <Icon as={CheckIcon} />
                        </Button>
                        <Button
                            colorScheme="red"
                            onClick={handleRedButtonClick}
                            size="lg" m={0.5} fontSize="28px"
                            title="Вернуть с замечаниями">
                            <Icon as={CloseIcon} />
                        </Button>
                        <Modal isOpen={isOpen} onClose={onClose}>
                            <ModalOverlay />
                            <ModalContent>
                                <ModalHeader>Введите текст замечания</ModalHeader>
                                <ModalCloseButton />
                                <ModalBody>
                                    <Textarea
                                        value={inputValue}
                                        onChange={handleInputChange}
                                        placeholder="Введите текст"
                                        size="sm"
                                    />
                                </ModalBody>
                                <ModalFooter>
                                    <Button colorScheme="blue" mr={3} onClick={handleSubmit}>
                                        Сохранить
                                    </Button>
                                    <Button variant="ghost" onClick={onClose}>Отмена</Button>
                                </ModalFooter>
                            </ModalContent>
                        </Modal>
                    </Box>
                );
            }
        },
        {
            accessorKey: "grade.activity",
            size: 66,
            cell: (props) => {
                const { isOpen, onOpen, onClose } = useDisclosure();

                const handleClick = () => {
                    onOpen();
                };

                const activity = props.row.original.grade.activity;

                return (
                    <>
                        <Button
                            colorScheme="yellow"
                            onClick={handleClick}
                            size="lg" m={0.5} fontSize="28px"
                            title="Просмотреть активность">
                            <Icon as={HamburgerIcon} />
                        </Button>
                        <Modal isOpen={isOpen} onClose={onClose}>
                            <ModalOverlay />
                            <ModalContent maxWidth="50vw" maxHeight="50vh">
                                <ModalHeader>Активность</ModalHeader>
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
        },
        {
            accessorKey: "grade.reference",
            size: 140,
            cell: props => {
                const handleGet = async () => {
                    try {
                        const response = await axios.get(`/works${props.getValue()}`, {
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

                const isDisabled = props.row.original.grade.status === "Не выполнена";

                return (
                    <Button
                        colorScheme="green"
                        onClick={handleGet}
                        size="lg" m={0.5} fontSize="28px"
                        disabled={isDisabled}
                        title="Открыть работу студента">
                        Работа
                    </Button>
                );
            }
        }
    ];

    useEffect(() => {
        if (subject) {
            const mappedData = subject.sgwdtos.map(item => {
                const updatedWork = {
                    ...item.work,
                    worktype: `${item.work.worktype} ${item.work.number}`
                };
                return {
                    ...item,
                    work: updatedWork
                };
            });

            const statusOrder = [
                "Ожидание проверки",
                "Замечания",
                "Не выполнена",
                "Истёк срок сдачи",
                "Выполнена"
            ];

            const sortedData = mappedData.sort((a, b) => {
                const statusIndexA = statusOrder.indexOf(a.grade.status);
                const statusIndexB = statusOrder.indexOf(b.grade.status);

                if (statusIndexA < statusIndexB) return -1;
                if (statusIndexA > statusIndexB) return 1;
                return 0;
            });

            setSgwdtos(sortedData);
        } else {
            setSgwdtos([]);
        }
    }, [data, selected]);

    const updateGrade = (updatedGrade) => {
        setSgwdtos(prev => prev.map(row => {
            if (row.grade.idStudent === updatedGrade.idStudent && row.work.idWork === updatedGrade.idWork) {
                return {
                    ...row,
                    grade: {
                        ...row.grade,
                        activity: updatedGrade.activity,
                        status: updatedGrade.status
                    }
                };
            }
            return row;
        }));
    };

    const table = useReactTable({
        data: sgwdtos,
        columns,
        getCoreRowModel: getCoreRowModel(),
        meta: {
            updateData: (rowIndex, columnId, value) => {
                setSgwdtos(prev => prev.map(
                    (row, index) =>
                        index === rowIndex ? {
                            ...row,
                            [columnId]: value,
                        } : row
                ));
            }
        }
    });

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleSave = () => {
        setIsEditing(false);
    };

    const handleCancel = () => {
        setIsEditing(false);
    };

    if (!sgwdtos.length) {
        return <Text>Нет данных для отображения</Text>;
    }
    console.log("Полная таблица", sgwdtos)
    return (
        <Box>
            <Box>
                <Text>{subject.subjectName}</Text>
                <Text>{subject.group}</Text>
                {isEditing ? (
                    <Box />
                ) : (
                    <Button colorScheme="yellow" onClick={handleEditClick}>Редактировать</Button>
                )}
            </Box>
            <Box>
                {isEditing ? (
                    <TeacherEditableTable subject={subject} onSave={handleSave} onCancel={handleCancel} />
                ) : (
                    <ViewTable table={table} />
                )}
            </Box>
        </Box>
    );
};

export default TeachersTable;
