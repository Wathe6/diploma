import { useState, useEffect } from "react";
import {Box, Button, Text, Icon, HStack, Select} from "@chakra-ui/react";
import {
  flexRender,
  useReactTable,
  getCoreRowModel,
  getSortedRowModel,
} from "@tanstack/react-table";
import axios from "axios";
import DATA from "../data";
import EditableCell from "./EditableCell";
import StatusCell from "./StatusCell";
import SortIcon from "./icons/SortIcon";
import TopArrow from "./icons/TopArrow";
import DownArrow from "./icons/DownArrow";
import IndeterminateCheckbox from "./IndeterminateCheckbox";

const tableOptions = [
  { value: 'extended-groups', label: 'Укрупнённые группы' },
  { value: 'fields-of-study', label: 'Направления подготовки' },
  { value: 'educational-groups', label: 'Образовательные программы' },
  { value: 'subjects', label: 'Предметы' },
  { value: 'teachers', label: 'Преподаватели' },
  { value: 'teach-subj', label: 'ПрепПред' },
  { value: 'students', label: 'Студенты' },
  { value: 'groups', label: 'Группы' },
  { value: 'works', label: 'Работы' },
  { value: 'grades', label: 'Оценки' },
  { value: 'users', label: 'Аккаунты'}
];

const AdminTable = () => {
  const [data, setData] = useState(DATA);
  const [columns, setColumns] = useState([]);
  const [originalData, setOriginalData] = useState([]);
  const [selectedOption, setSelectedOption] = useState(tableOptions[0].value);
  const [errorMessage, setErrorMessage] = useState(null);
  const [enums, setEnums] = useState([]);
  const [rowSelection, setRowSelection] = useState({});

  useEffect(() => {
    fetchEnumData();
  }, []);

  useEffect(() => {
    console.log("enums", enums);
  }, [enums]);

  const fetchEnumData = async () => {
    try {
      const response = await axios.get("api/enums/all");
      const buffer = {};

      Object.keys(response.data[0]).forEach(key => {
        const enumKey = key.slice(0, -1);
        buffer[enumKey] = response.data[0][key];
      })

      setEnums(buffer);
    } catch (error) {
      setErrorMessage(error.response.data);
      console.error("Error fetching data:", error);
    }
  }

  useEffect(() => {
    fetchData();
  }, [selectedOption]);

  //Запрашиваем у сервера выбранную таблицу
  const fetchData = async () => {
    try {
      const response = await axios.get(`api/${selectedOption}`);
      setData(response.data);
      setOriginalData(response.data);
      setColumns(generateColumns(response.data));
    } catch (error) {
      setErrorMessage(error.response.data);
      console.error("Error fetching data:", error);
    }
  };

  const handleOptionChange = (event) => {
    table.setState(prevState => {
      return {
        ...prevState,
        columnSizing: []
      };
    });

    setSelectedOption(event.target.value);
  };
  //Собираем заголовки для таблицы
  const generateColumns = (data) => {
    const newColumns = Object.keys(data[0]).map(key => {
      let maxSize;
      if(key.length > 10)
        maxSize = key.length * 7 + 25;
      else
        maxSize = 120;
      data.forEach(row => {
        const cellSize = String(row[key]).length * 8 + 10;
        if (cellSize > maxSize) {
          maxSize = cellSize;
        }
      });

      return {
        accessorKey: key,
        header: key,
        size: maxSize,
        cell: Array.isArray(enums[key]) ? StatusCell : EditableCell,
      };
    });

    // Добавляем новый заголовок в начало массива columns
    newColumns.unshift({
      id: 'select-col',
      size: 30,
      header: ({ table }) => (
          <IndeterminateCheckbox
              style={{
                position: "relative",
                display: "flex",
                width: "30px"
              }}
              {...{
                checked: table.getIsAllRowsSelected(),
                indeterminate: table.getIsSomeRowsSelected(),
                onChange: table.getToggleAllRowsSelectedHandler(),
              }}
          />
      ),
      cell: ({ row }) => (
          <IndeterminateCheckbox
              style={{
                position: "relative",
                display: "flex",
                width: "30px"
              }}
              {...{
                checked: row.getIsSelected(),
                disabled: !row.getCanSelect(),
                indeterminate: row.getIsSomeSelected(),
                onChange: row.getToggleSelectedHandler(),
              }}
          />
      ),
    });
    return newColumns;
  };
  const handleAddRow = () => {
    const newRow = {};
    columns.forEach(column => {
      if (column.accessorKey) {
        newRow[column.accessorKey] = Array.isArray(enums[column.accessorKey]) ? enums[column.accessorKey][0] : "";
      }
    });
    setData(data => [...data, newRow]);
  };

  const handleSave = async () => {
    const modifiedRows = data.filter((row, index) => {
      // Сравниваем значения как строки, а не числа
      return JSON.stringify(row) !== JSON.stringify(originalData[index]);
    }).map(row => {
      // Преобразуем все числовые значения в числа
      return Object.fromEntries(
        Object.entries(row).map(([key, value]) => [key, typeof value === 'string' && !isNaN(Number(value)) ? Number(value) : value])
      );
    });
    console.log("Data: ", data);
    console.log("originalData: ", originalData);

    if (modifiedRows.length > 0) { // Проверяем, есть ли модифицированные данные
      try {
        console.log("Server POST: ", modifiedRows);
        await axios.post(`api/${selectedOption}`, modifiedRows);
        setErrorMessage(null);
      } catch (error) {
        setErrorMessage(error.response.data);
        console.error("Error saving data:", error);
      }
    } else {
      console.log("No modified data to save.");
    }
  };

  const handleDelete = async () => {
    try {
      const selectedRows = table.getSelectedRowModel().flatRows.map(row => row.original);
      console.log(selectedRows);

      //Отправка DELETE запроса на сервер
      await axios.delete(`api/${selectedOption}`, {
        data: selectedRows
      });

      // Обновление состояния или выполнение других действий после успешного удаления
      fetchData();
    } catch (error) {
      // Обработка ошибок при удалении
      console.error('Error deleting rows:', error);
    }
  };

  const table = useReactTable({
    data,
    columns,
    defaultColumn: {
      minSize: 100,
      maxSize: 800,
    },
    getCoreRowModel : getCoreRowModel(),
    columnResizeMode : "onChange",
    getSortedRowModel : getSortedRowModel(),
    meta : {
      updateData : (rowIndex, columnId, value) => setData(
        prev => prev.map(
          (row, index) =>
            index === rowIndex ? {
              ...prev[rowIndex],
              [columnId]:value,
            }: row
        )
      )
    },
    state : {
      rowSelection: rowSelection,
    },
    getRowId: row => row.uuid,
    onRowSelectionChange : setRowSelection,
    enableRowSelection : true,
  });

  //console.log(data);
  return (
    <Box >
      <Box styles={{width: table.getTotalSize()}}>
        <HStack spacing={3}>
          <Button onClick={handleSave} minW="60px" colorScheme="blue" size="sm">Сохранить</Button>
          <Button onClick={handleDelete} minW="40px"  colorScheme="red" size="sm">Удалить</Button>
          <Select value={selectedOption} mb="1px" onChange={handleOptionChange}>
            {tableOptions.map(option => (
                <option key={option.value} value={option.value}>{option.label}</option>
            ))}
          </Select>
        </HStack>
      </Box>
      <Box className="table" styles={{width: table.getTotalSize()}}>
        {table.getHeaderGroups().map((headerGroup) => (
          <Box className="tr" key={headerGroup.id}>
            {headerGroup.headers.map( (header) => (
              header.id === 'select-col' ? (
                <Box className="th" style={{ width: 30 }} key={header.id}>
                  {flexRender(header.column.columnDef.header, header.getContext())}
                </Box>
              ) : (
                <Box className="th" style={{ width: header.getSize() }} key={header.id}>
                  {header.column.columnDef.header}
                  {header.column.getCanSort()}
                  {{
                      asc: (<Icon as={TopArrow} fontSize={14} onClick={header.column.getToggleSortingHandler()}/>),
                      desc: (<Icon as={DownArrow} fontSize={14} onClick={header.column.getToggleSortingHandler()}/>),
                      false: (<Icon as={SortIcon} fontSize={14} onClick={header.column.getToggleSortingHandler()}/>),
                    }[header.column.getIsSorted()]}
                  <Box
                    onMouseDown={header.getResizeHandler()}
                    onTouchStart={header.getResizeHandler()}
                    className={'resizer ${ header.column.getIsResizing() ? "isResizing" : "" }'}
                  />
                </Box>
              )
            ))}
          </Box>
        ))}
        {table.getRowModel().rows.map((row) => (
            <Box className="tr" key={row.id}>
              {row.getVisibleCells().map((cell) => (
                  cell.column.id === 'select-col' ? (
                      <Box className="td"  style={{ width: 30}} key={cell.id}>
                        {flexRender(cell.column.columnDef.cell, {...cell.getContext(), enums})}
                      </Box>
                  ) : (
                      <Box className="td" style={{ width: cell.column.getSize()}} key={cell.id}>
                        {flexRender(cell.column.columnDef.cell, {...cell.getContext(), enums})}
                      </Box>
                  )
              ))}
            </Box>
        ))}
        <Box onClick={handleAddRow} textAlign="center" display="flex" flexDirection="column" h="10" alignItems="center" _hover={{ bg: "gray.500" }}>
          <Box style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100%' }}>
            <img src="plus-circle.svg" alt="Add" />
          </Box>
        </Box>
      </Box>
      {<div>Для множественной сортировки зажмите Shift.</div>}
      {errorMessage && <Text fontSize='25px' color="tomato">Error: {errorMessage}</Text>}
      {/*<pre style={{ minHeight: '10rem' }}>*/}
      {/*  {JSON.stringify(*/}
      {/*      {*/}
      {/*        columnSizing: table.getState().columnSizing,*/}
      {/*      },*/}
      {/*      null,*/}
      {/*      2*/}
      {/*  )}*/}
      {/*</pre>*/}
    </Box>
  );
};
export default AdminTable;