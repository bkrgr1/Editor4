package de.bkroeger.editor4.runtime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseRuntime {

	protected IRuntime parent = null;

}
