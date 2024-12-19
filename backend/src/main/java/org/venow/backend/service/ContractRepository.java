package org.venow.backend.service;

import org.venow.backend.service.dto.ContractDTO;

public interface ContractRepository  {


    ContractDTO save(ContractDTO contractDTO);
}
