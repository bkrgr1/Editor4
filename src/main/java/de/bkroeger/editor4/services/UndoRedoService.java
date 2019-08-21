package de.bkroeger.editor4.services;

import java.util.Stack;

import org.springframework.stereotype.Service;

@Service
public class UndoRedoService {

	private Stack<StackEntry> undoStack = new Stack<>();
	
	private Stack<StackEntry> redoStack = new Stack<>();
	
	public void pushUndo(StackEntry entry) {
		synchronized(undoStack) {
			this.undoStack.push(entry);
		}
	}
	
	public StackEntry popUndo() {
		synchronized(undoStack) {
			return this.undoStack.pop();
		}
	}
	
	public void pushRedo(StackEntry entry) {
		synchronized(redoStack) {
			this.redoStack.push(entry);
		}
	}
	
	public StackEntry popRedo() {
		synchronized(redoStack) {
			return this.redoStack.pop();
		}
	}
}
