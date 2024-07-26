import React from 'react';
import {Box} from '@chakra-ui/react';
import {flexRender} from "@tanstack/react-table";

const ViewTable = ({table}) => {
    if (!table) {
        return <Text>Нет данных для отображения</Text>;
    }
    return (
        <Box className="table" style={{ width: table.getTotalSize() }}>
            {table.getHeaderGroups().map((headerGroup) => (
                <Box className="tr" key={headerGroup.id}>
                    {headerGroup.headers.map( (header) => (
                        <Box className="th" style={{ width: header.getSize() }} key={header.id}>
                            {header.column.columnDef.header}
                        </Box>
                    ))}
                </Box>
            ))}
            {table.getRowModel().rows.map(row => (
                <Box className="tr" key={row.id}>
                    {row.getVisibleCells().map(cell =>
                        <Box className="td" style={{ width: cell.column.getSize()}} key={cell.id}>
                            {flexRender(
                                cell.column.columnDef.cell,
                                cell.getContext())}
                        </Box>
                    )}
                </Box>))
            }
        </Box>
    );
}
export default ViewTable;