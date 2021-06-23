<?php
namespace metastore;

/**
 * Autogenerated by Thrift Compiler (0.14.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
use Thrift\Base\TBase;
use Thrift\Type\TType;
use Thrift\Type\TMessageType;
use Thrift\Exception\TException;
use Thrift\Exception\TProtocolException;
use Thrift\Protocol\TProtocol;
use Thrift\Protocol\TBinaryProtocolAccelerated;
use Thrift\Exception\TApplicationException;

class PartitionValuesResponse
{
    static public $isValidate = false;

    static public $_TSPEC = array(
        1 => array(
            'var' => 'partitionValues',
            'isRequired' => true,
            'type' => TType::LST,
            'etype' => TType::STRUCT,
            'elem' => array(
                'type' => TType::STRUCT,
                'class' => '\metastore\PartitionValuesRow',
                ),
        ),
    );

    /**
     * @var \metastore\PartitionValuesRow[]
     */
    public $partitionValues = null;

    public function __construct($vals = null)
    {
        if (is_array($vals)) {
            if (isset($vals['partitionValues'])) {
                $this->partitionValues = $vals['partitionValues'];
            }
        }
    }

    public function getName()
    {
        return 'PartitionValuesResponse';
    }


    public function read($input)
    {
        $xfer = 0;
        $fname = null;
        $ftype = 0;
        $fid = 0;
        $xfer += $input->readStructBegin($fname);
        while (true) {
            $xfer += $input->readFieldBegin($fname, $ftype, $fid);
            if ($ftype == TType::STOP) {
                break;
            }
            switch ($fid) {
                case 1:
                    if ($ftype == TType::LST) {
                        $this->partitionValues = array();
                        $_size575 = 0;
                        $_etype578 = 0;
                        $xfer += $input->readListBegin($_etype578, $_size575);
                        for ($_i579 = 0; $_i579 < $_size575; ++$_i579) {
                            $elem580 = null;
                            $elem580 = new \metastore\PartitionValuesRow();
                            $xfer += $elem580->read($input);
                            $this->partitionValues []= $elem580;
                        }
                        $xfer += $input->readListEnd();
                    } else {
                        $xfer += $input->skip($ftype);
                    }
                    break;
                default:
                    $xfer += $input->skip($ftype);
                    break;
            }
            $xfer += $input->readFieldEnd();
        }
        $xfer += $input->readStructEnd();
        return $xfer;
    }

    public function write($output)
    {
        $xfer = 0;
        $xfer += $output->writeStructBegin('PartitionValuesResponse');
        if ($this->partitionValues !== null) {
            if (!is_array($this->partitionValues)) {
                throw new TProtocolException('Bad type in structure.', TProtocolException::INVALID_DATA);
            }
            $xfer += $output->writeFieldBegin('partitionValues', TType::LST, 1);
            $output->writeListBegin(TType::STRUCT, count($this->partitionValues));
            foreach ($this->partitionValues as $iter581) {
                $xfer += $iter581->write($output);
            }
            $output->writeListEnd();
            $xfer += $output->writeFieldEnd();
        }
        $xfer += $output->writeFieldStop();
        $xfer += $output->writeStructEnd();
        return $xfer;
    }
}
