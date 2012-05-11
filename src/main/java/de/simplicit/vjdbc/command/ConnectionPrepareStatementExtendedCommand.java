// VJDBC - Virtual JDBC
// Written by Michael Link
// Website: http://vjdbc.sourceforge.net

package de.simplicit.vjdbc.command;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPrepareStatementExtendedCommand implements Command {
    private static final long serialVersionUID = 3760559793366120249L;

    private String _sql;
    private int _autoGeneratedKeys;
    private int[] _columnIndexes;
    private String[] _columnNames;

    public ConnectionPrepareStatementExtendedCommand() {
    }

    public ConnectionPrepareStatementExtendedCommand(String sql, int autoGeneratedKeys) {
        _sql = sql;
        _autoGeneratedKeys = autoGeneratedKeys;
    }

    public ConnectionPrepareStatementExtendedCommand(String sql, int[] columnIndexes) {
        _sql = sql;
        _columnIndexes = columnIndexes;
    }

    public ConnectionPrepareStatementExtendedCommand(String sql, String[] columnNames) {
        _sql = sql;
        _columnNames = columnNames;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(_sql);
        out.writeInt(_autoGeneratedKeys);
        out.writeObject(_columnIndexes);
        out.writeObject(_columnNames);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        _sql = in.readUTF();
        _autoGeneratedKeys = in.readInt();
        _columnIndexes = (int[])in.readObject();
        _columnNames = (String[])in.readObject();
    }

    public Object execute(Object target, ConnectionContext ctx) throws SQLException {
        // Resolve and check the query
        String sql = ctx.resolveOrCheckQuery(_sql);
        // Now make the descision what call to execute
        if(_columnIndexes != null) {
            return ((Connection)target).prepareStatement(sql, _columnIndexes);
        }
        else if(_columnNames != null) {
            return ((Connection)target).prepareStatement(sql, _columnNames);
        }
        else {
            return ((Connection)target).prepareStatement(sql, _autoGeneratedKeys);
        }
    }

    public String toString() {
        return "ConnectionPrepareStatementCommand";
    }
}