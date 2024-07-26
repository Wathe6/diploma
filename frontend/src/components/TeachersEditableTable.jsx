import React, { useState, useEffect, useRef } from 'react';
import {
    Box,
    Button,
    Input,
    Text,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalFooter,
    ModalBody,
    ModalCloseButton,
    Select,
    Icon
} from '@chakra-ui/react';
import { getCoreRowModel, useReactTable } from "@tanstack/react-table";
import axios from "axios";
import ViewTable from "./ViewTable.jsx";
import EditableCell from "./EditableCell.jsx";
import StatusCell from "./StatusCell.jsx";
import PageIcon from "./icons/PageIcon.jsx";

const enums = {
    worktype: ['Лр', 'Пр', 'КП', 'Кр', 'Рр', 'ВКР', 'практика'],
};

const columns =[
    {
        header: <Text>Тип</Text>,
        accessorKey: "worktype",
        size: 100,
        cell: (props) => <StatusCell {...props} enums={enums} />,
    },{
        header: <Text>Номер</Text>,
        accessorKey: "number",
        size: 110,
        cell : EditableCell
    },{
        header: <Text>Дата открытия</Text>,
        accessorKey: "dateOpen",
        size: 250,
        cell: EditableCell
    },{
        header: <Text>Дата окончания</Text>,
        accessorKey: "dateEnd",
        size: 250,
        cell: EditableCell
    },{
        header: <Text>Файл</Text>,
        accessorKey: "reference",
        size: 450,
        cell: props => {
            const fileInputRef = useRef(null);
            const [isModalOpen, setIsModalOpen] = useState(false);
            const [pageNumber, setPageNumber] = useState('');
            const [currentProps, setCurrentProps] = useState(null);

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

                    // Добавление параметра страницы к URL, если он существует
                    const finalURL = pageParam ? `${fileURL}#page=${pageParam}` : fileURL;

                    // Открытие PDF файла в новой вкладке
                    window.open(finalURL, '_blank');
                } catch (error) {
                    console.error('Error:', error);
                }
            };

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
                formData.append('props', JSON.stringify(props));

                try {
                    const response = await axios.post(`/docs${props.reference}`, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                    console.log('Response:', response);
                } catch (error) {
                    console.error('Error:', error);
                }
            };

            const openModal = (props) => {
                setCurrentProps(props);
                setIsModalOpen(true);
            };

            const handleModalSave = async () => {
                if (!currentProps || !pageNumber) return;
                const newReference = currentProps.reference.replace(/page=\d+/, `page=${pageNumber}`);
                try {
                    console.log("Post work", [{ ...currentProps, reference: newReference }]);
                    await axios.post(`/api/works`, [{ ...currentProps, reference: newReference }]);
                    setIsModalOpen(false);
                } catch (error) {
                    console.error('Error:', error);
                }
            };

            return (
                <>
                    <Button
                        colorScheme="teal"
                        size="lg" m={0.5} fontSize="28px"
                        onClick={() => handleGet(props)}>
                        Открыть
                    </Button>
                    <Input
                        type="file"
                        accept="application/pdf"
                        ref={fileInputRef}
                        style={{ display: 'none' }}
                    />
                    <Button
                        colorScheme="red"
                        onClick={() => handleButtonClick(props.row.original)}
                        size="lg" m={0.5} fontSize="28px">
                        Изменить
                    </Button>
                    <Button
                        colorScheme="blue"
                        onClick={() => openModal(props.row.original)}
                        size="lg" m={0.5} fontSize="28px"
                        title="Изменить страницу открытия">
                        <Icon as={PageIcon} />
                    </Button>

                    <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                        <ModalOverlay />
                        <ModalContent>
                            <ModalHeader>Изменить номер страницы</ModalHeader>
                            <ModalCloseButton />
                            <ModalBody>
                                <Input
                                    placeholder="Введите номер страницы"
                                    value={pageNumber}
                                    onChange={(e) => setPageNumber(e.target.value)}
                                />
                            </ModalBody>
                            <ModalFooter>
                                <Button colorScheme="blue" mr={3} onClick={handleModalSave}>Сохранить</Button>
                                <Button variant="ghost" onClick={() => setIsModalOpen(false)}>Отмена</Button>
                            </ModalFooter>
                        </ModalContent>
                    </Modal>
                </>
            );
        }
    }
];

const TeacherEditableTable = ({ subject, onSave, onCancel }) => {
    const [data, setData] = useState([]);
    const [uniqueWorks, setUniqueWorks] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newRow, setNewRow] = useState({ worktype: enums.worktype[0], number: '', dateOpen: '', dateEnd: '', idSubject: subject.idSubject });

    if (!subject) {
        return <Text>Нет данных для отображения</Text>;
    }

    useEffect(() => {
        const computedUniqueWorks = subject.sgwdtos
            .map(item => item.work)
            .filter((work, index, self) =>
                index === self.findIndex((w) => w.idWork === work.idWork)
            );

        setUniqueWorks(computedUniqueWorks);
        setData(computedUniqueWorks);
    }, [subject]);

    const handleSave = async () => {
        const modifiedRows = data.filter((row, index) => {
            return JSON.stringify(row) !== JSON.stringify(uniqueWorks[index]);
        }).map(row => {
            return Object.fromEntries(
                Object.entries(row).map(([key, value]) => {
                    if (key === 'number' && typeof value === 'string' && !isNaN(Number(value))) {
                        return [key, Number(value)];
                    }
                    return [key, value];
                })
            );
        });

        if (modifiedRows.length > 0) {
            try {
                await axios.post(`api/works`, modifiedRows);
                console.log("Post api/works", modifiedRows);
            } catch (error) {
                console.error("Error saving data:", error);
            }
        } else {
            console.log("No modified data to save.");
        }
        onSave();
    };

    const handleAddRow = () => {
        setNewRow({ worktype: enums.worktype[0], number: '', dateOpen: '', dateEnd: '', idSubject: subject.idSubject });
        setIsModalOpen(true);
    };

    const handleModalSave = async () => {
        const newRowWithNumber = { ...newRow, number: Number(newRow.number) };
        try {
            await axios.post(`/create/work`, newRowWithNumber);
            setData(data => [...data, newRowWithNumber]);
            setIsModalOpen(false);
            setNewRow({ worktype: enums.worktype[0], number: '', dateOpen: '', dateEnd: '', idSubject: subject.idSubject });
        } catch (error) {
            console.error("Error creating new work:", error);
        }
    };

    const table = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
        meta: {
            updateData: (rowIndex, columnId, value) => setData(
                prev => prev.map(
                    (row, index) =>
                        index === rowIndex ? {
                            ...prev[rowIndex],
                            [columnId]: value,
                        } : row
                )
            )
        }
    });

    return (
        <Box>
            <Box>
                <Button colorScheme="blue" onClick={handleSave}>Сохранить</Button>
                <Button colorScheme="red" onClick={onCancel}>Отмена</Button>
            </Box>
            <ViewTable table={table} />
            <Box className="table" styles={{ width: table.getTotalSize() }}>
                <Box onClick={handleAddRow} textAlign="center" display="flex" flexDirection="column" h="10" alignItems="center" _hover={{ bg: "gray.500" }}>
                    <Box style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100%' }}>
                        <img src="plus-circle.svg" alt="Add" />
                    </Box>
                </Box>
            </Box>

            {/* Modal for Adding New Row */}
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Добавить новую запись</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>
                        <Select value={newRow.worktype} onChange={(e) => setNewRow({ ...newRow, worktype: e.target.value })}>
                            {enums.worktype.map(type => (
                                <option key={type} value={type}>{type}</option>
                            ))}
                        </Select>
                        <Input
                            placeholder="Номер"
                            value={newRow.number}
                            onChange={(e) => setNewRow({ ...newRow, number: e.target.value })}
                        />
                        <Input
                            placeholder="Дата открытия"
                            value={newRow.dateOpen}
                            onChange={(e) => setNewRow({ ...newRow, dateOpen: e.target.value })}
                        />
                        <Input
                            placeholder="Дата окончания"
                            value={newRow.dateEnd}
                            onChange={(e) => setNewRow({ ...newRow, dateEnd: e.target.value })}
                        />
                    </ModalBody>
                    <ModalFooter>
                        <Button colorScheme="blue" mr={3} onClick={handleModalSave}>Сохранить</Button>
                        <Button variant="ghost" onClick={() => setIsModalOpen(false)}>Отмена</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </Box>
    );
}

export default TeacherEditableTable;
